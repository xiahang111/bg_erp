package com.bingo.erp.xo.order.tools;

import com.bingo.erp.base.enums.*;
import com.bingo.erp.base.fatocry.material.MaterialCalculateFactory;
import com.bingo.erp.base.vo.CustomerVO;
import com.bingo.erp.commons.entity.IronwareInfo;
import com.bingo.erp.commons.entity.MaterialInfo;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.commons.entity.TransomInfo;
import com.bingo.erp.commons.feign.PersonFeignClient;
import com.bingo.erp.utils.JsonUtils;
import com.bingo.erp.xo.order.enums.MaterialFactoryEnum;
import com.bingo.erp.xo.order.global.ExcelConf;
import com.bingo.erp.xo.order.global.NormalConf;
import com.bingo.erp.xo.order.global.SysConf;
import com.bingo.erp.xo.order.mapper.IronwareInfoMapper;
import com.bingo.erp.xo.order.mapper.MaterialInfoMapper;
import com.bingo.erp.xo.order.mapper.TransomMapper;
import com.bingo.erp.xo.order.vo.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.jxls.util.Util;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 订单方法集中类，先放在这里，以后再重构订单的生成方法
 */
@Slf4j
@Component
public class OrderTools {



    @Resource
    private RabbitTemplate rabbitTemplate;

    public boolean materialValidate(List<MaterialInfoVO> materialInfoVOS) {

        Set<String> keys = new HashSet<>();
        materialInfoVOS.stream().forEach(materialInfoVO -> {
            String key = materialInfoVO.getMaterialType() + materialInfoVO.getMaterialColor() +
                    materialInfoVO.getHandleType() + materialInfoVO.getGlassColor() + "";
            keys.add(key);
        });

        if (keys.size() > 1) {
            return false;
        }
        return true;

    }

    public Map<String, List<MaterialInfoVO>> materialsToMap(List<MaterialInfoVO> materialInfoVOS) {

        Map<String, List<MaterialInfoVO>> map = new HashMap<>();

        for (MaterialInfoVO infoVO : materialInfoVOS) {

            String key = MaterialColorEnums.getEnumByCode(infoVO.getMaterialColor()) + "、" +
                    MaterialTypeEnums.getEnumByCode(infoVO.getMaterialType()) + "(" +
                    HandleEnums.getNameByCode(infoVO.getHandleType()) + ")、" +
                    GlassColor.getNameByCode(infoVO.getGlassColor()) + "玻璃";

            List<MaterialInfoVO> infoVOS = map.get(key);

            if (null == infoVOS) {

                infoVOS = new ArrayList<>();
                infoVOS.add(infoVO);
                map.put(key, infoVOS);
            } else {
                infoVOS.add(infoVO);
            }

        }

        return map;
    }

    public int extensionExcel(HSSFSheet sheet, Map<String, List<MaterialInfoVO>> map, int ironwareNum, int transomNum, boolean isHaveTransom) {


        int addNum = (map.keySet().size() - 1) * 2;

        for (String key : map.keySet()) {

            addNum += map.get(key).size();

        }

        addNum = addNum - 1;

        sheet.shiftRows(10, sheet.getLastRowNum(), addNum, true, false);


        int ia = 0;
        int rowNum = 10;
        int point = 0;
        for (String key : map.keySet()) {

            if (ia == 0) {

                int add = map.get(key).size() - 1;

                for (int j = 0; j < add; j++) {
                    Util.copyRow(sheet, sheet.getRow(9), sheet.getRow(rowNum + j));
                }

                point = rowNum + add;
                ia++;

            } else {
                int add = map.get(key).size() + 2;

                for (int i = 0; i < add; i++) {

                    if (i == 0) {
                        Util.copyRow(sheet, sheet.getRow(7), sheet.getRow(point + i));
                    }

                    if (i == 1) {
                        Util.copyRow(sheet, sheet.getRow(8), sheet.getRow(point + i));

                    }

                    if (i > 1) {
                        Util.copyRow(sheet, sheet.getRow(9), sheet.getRow(point + i));

                    }

                }

                point = point + add;
            }


        }

        if (transomNum > 0) {
            int TRANSOM_START_NUM = 13 + addNum;
            sheet.shiftRows(TRANSOM_START_NUM, sheet.getLastRowNum(), transomNum, true, false);
            for (int i = 0; i < transomNum; i++) {
                Util.copyRow(sheet, sheet.getRow(TRANSOM_START_NUM - 1), sheet.getRow(TRANSOM_START_NUM + i));
            }
        }


        if (ironwareNum > 0) {
            int IRONWARE_START_NUM;
            if (isHaveTransom || transomNum > 0) {
                IRONWARE_START_NUM = 16 + addNum + (transomNum);
            } else {
                IRONWARE_START_NUM = ExcelConf.IRONWARE_START_NUM + addNum;
            }

            sheet.shiftRows(IRONWARE_START_NUM, sheet.getLastRowNum(), ironwareNum, true, false);

            for (int i = 0; i < ironwareNum; i++) {
                Util.copyRow(sheet, sheet.getRow(IRONWARE_START_NUM - 1), sheet.getRow(IRONWARE_START_NUM + i));
            }
        }

        return addNum;


    }

    private int getIronNum(MaterialVO materialVO) {

        List<IronwareInfoVO> ironwareInfoVOS = materialVO.getIronwares();
        //计算角码和螺丝

        Map<String, Integer> cornerMap = new HashMap<>();
        for (MaterialInfoVO materialInfoVO : materialVO.getMaterials()) {

            if (CornerMaterialEnums.None.code != materialInfoVO.getCornerMaterial()) {
                String cornerName = CornerMaterialEnums.getEnumByCode(materialInfoVO.getCornerMaterial());

                Integer cornerNumNow = cornerMap.get(cornerName);

                Integer cornerNum = materialInfoVO.getCornerNum();

                //五金数量
                if (null == cornerNumNow) {

                    cornerMap.put(cornerName, cornerNum);

                } else {
                    cornerMap.put(cornerName, cornerMap.get(cornerName) + cornerNum);
                }

                //螺丝数量

                if (null == cornerMap.get(NormalConf.SCREW_NAME)) {
                    cornerMap.put(NormalConf.SCREW_NAME, materialInfoVO.getScrewNum());
                } else {
                    cornerMap.put(NormalConf.SCREW_NAME, cornerMap.get(NormalConf.SCREW_NAME) + materialInfoVO.getScrewNum());
                }
            }

        }

        for (String key : cornerMap.keySet()) {

            IronwareInfoVO ironwareInfoVO = new IronwareInfoVO();

            ironwareInfoVO.setIronwareName(key);
            ironwareInfoVO.setIronwareNum(cornerMap.get(key));
            ironwareInfoVO.setUnit("个");
            ironwareInfoVO.setIronwareColor(0);
            ironwareInfoVO.setSpecification("");
            ironwareInfoVO.setRemark("");

            ironwareInfoVOS.add(ironwareInfoVO);
        }

        return ironwareInfoVOS.size() - 1;


    }

    public int productExtensionExcel(HSSFSheet sheet, Map<String, List<MaterialInfoVO>> map, MaterialVO materialVO) {

        int transomNum = 0;

        if (materialVO.isHaveTransom && null != materialVO.getTransoms() && materialVO.getTransoms().size() > 0) {
            transomNum = materialVO.getTransoms().size() - 1;
        }


        int ironwareNum = getIronNum(materialVO);

        int addNum = (map.keySet().size() - 1) * 2;

        for (String key : map.keySet()) {

            addNum += map.get(key).size();

        }

        addNum = addNum - 1;

        sheet.shiftRows(10, sheet.getLastRowNum(), addNum, true, false);


        int ia = 0;
        int rowNum = 10;
        int a = 1;
        int point = 0;
        for (String key : map.keySet()) {

            if (ia == 0) {

                int add = map.get(key).size() - 1;

                for (int j = 0; j < add; j++) {
                    Util.copyRow(sheet, sheet.getRow(9), sheet.getRow(rowNum + j));
                }

                point = rowNum + add;
                ia++;

            } else {
                int add = map.get(key).size() + 2;

                for (int i = 0; i < add; i++) {

                    validateCell(sheet.getRow(point + i));

                    if (i == 0) {
                        Util.copyRow(sheet, sheet.getRow(7), sheet.getRow(point + i));
                    }

                    if (i == 1) {
                        log.info("a ===" + a++);
                        Util.copyRow(sheet, sheet.getRow(8), sheet.getRow(point + i));


                    }

                    if (i > 1) {
                        Util.copyRow(sheet, sheet.getRow(9), sheet.getRow(point + i));

                    }

                }

                point = point + add;
            }


        }


        int rowNum_glass = 13 + addNum;

        sheet.shiftRows(rowNum_glass, sheet.getLastRowNum(), addNum, true, false);

        int ia1 = 0;
        int point11 = 0;
        for (String key : map.keySet()) {

            if (ia1 == 0) {

                int add = map.get(key).size() - 1;

                for (int j = 0; j < add; j++) {
                    Util.copyRow(sheet, sheet.getRow(12 + addNum), sheet.getRow(rowNum_glass + j));
                }

                point11 = 12 + addNum + add + 1;
                ia1++;

            } else {

                int add = map.get(key).size() + 2;

                for (int i = 0; i < add; i++) {

                    validateCell(sheet.getRow(point11 + i));

                    if (i == 0) {
                        Util.copyRow(sheet, sheet.getRow(10 + addNum), sheet.getRow(point11 + i));
                    }

                    if (i == 1) {
                        Util.copyRow(sheet, sheet.getRow(11 + addNum), sheet.getRow(point11 + i));

                    }

                    if (i > 1) {
                        Util.copyRow(sheet, sheet.getRow(12 + addNum), sheet.getRow(point11 + i));

                    }

                }

                point11 = point11 + add;
                ia1++;

            }

        }

        if (transomNum > 0) {
            int TRANSOM_START_NUM = 16 + addNum * 2;
            sheet.shiftRows(TRANSOM_START_NUM, sheet.getLastRowNum(), transomNum, true, false);
            for (int i = 0; i < transomNum; i++) {
                Util.copyRow(sheet, sheet.getRow(TRANSOM_START_NUM - 1), sheet.getRow(TRANSOM_START_NUM + i));
            }
        }


        if (ironwareNum > 0) {

            //存放螺丝和角码

            int IRONWARE_START_NUM;
            if (materialVO.isHaveTransom || transomNum > 0) {
                IRONWARE_START_NUM = 19 + addNum * 2 + (transomNum);
            } else {
                IRONWARE_START_NUM = 16 + addNum * 2;
            }


            sheet.shiftRows(IRONWARE_START_NUM, sheet.getLastRowNum(), ironwareNum, true, false);

            for (int i = 0; i < ironwareNum; i++) {
                Util.copyRow(sheet, sheet.getRow(IRONWARE_START_NUM - 1), sheet.getRow(IRONWARE_START_NUM + i));
            }
        }


        return addNum * 2;
    }

    /**
     * 料玻信息计算
     *
     * @param materialInfoVOS
     * @throws Exception
     */
    public void materialCalculate(List<MaterialInfoVO> materialInfoVOS) throws Exception {


        for (MaterialInfoVO materialInfoVO : materialInfoVOS) {

            //获取产品ID
            int materialType = materialInfoVO.getMaterialType();

            MaterialCalculateFactory factory = (MaterialCalculateFactory) MaterialFactoryEnum.getFactoryClass(Integer.valueOf(materialType)).newInstance();
            MaterialCalculateResultVO vo = (MaterialCalculateResultVO) factory.calculate(materialInfoVO);

            materialInfoVO.setMaterialHeight(vo.getMaterialHeight());
            materialInfoVO.setMaterialWidth(vo.getMaterialWidth());

            materialInfoVO.setGlassHeight(vo.getGlassHeight());
            materialInfoVO.setGlassWidth(vo.getGlassWidth());

            materialInfoVO.setMaterialDetail(vo.getMaterialDetail());

            //计算角码数量和螺丝数量

            materialInfoVO.setCornerNum(CornerMaterialEnums.getByCode(materialInfoVO.getCornerMaterial()).cornerNum * materialInfoVO.getMaterialNum());
            materialInfoVO.setScrewNum(CornerMaterialEnums.getByCode(materialInfoVO.getCornerMaterial()).screwNum * materialInfoVO.getCornerNum());

            BigDecimal singleArea = materialInfoVO.getHeight().
                    multiply(materialInfoVO.getWidth()).divide(NormalConf.divideNum).setScale(5, BigDecimal.ROUND_HALF_UP);

            BigDecimal minArea;

            //判定最小计算面积s
            // if (materialInfoVO.getMaterialType())
            switch (materialInfoVO.getMaterialType()) {
                case 2001:
                    minArea = new BigDecimal("0.8");
                    break;
                case 4001:
                    minArea = new BigDecimal("0.8");
                    break;
                case 5003:
                    minArea = new BigDecimal("0.8");
                    break;
                case 6001:
                    minArea = new BigDecimal("0.8");
                    break;
                case 7001:
                    minArea = new BigDecimal("0.8");
                    break;
                case 7002:
                    minArea = new BigDecimal("0.8");
                    break;
                case 7003:
                    minArea = new BigDecimal("0.8");
                    break;
                default:
                    minArea = new BigDecimal("0.5");
                    break;
            }


            if (singleArea.compareTo(minArea) < 0) {
                singleArea = minArea;
            }

            materialInfoVO.setArea(
                    singleArea.
                            multiply(new BigDecimal(materialInfoVO.getMaterialNum())).setScale(2, BigDecimal.ROUND_HALF_UP)
            );

            materialInfoVO.setTotalPrice(materialInfoVO.getArea().multiply(materialInfoVO.getPrice()).setScale(0, BigDecimal.ROUND_HALF_UP));

        }


    }

    public void transomCalculate(List<TransomVO> transomVOS) {

        if (transomVOS.size() > 0) {
            transomVOS.stream().forEach(transomVO -> {

                BigDecimal height = transomVO.getHeight();
                BigDecimal minHeight = new BigDecimal(1000);
                if (height.compareTo(minHeight) < 0) {
                    height = new BigDecimal(1000);
                }


                transomVO.setTotalPrice(transomVO.getPrice().multiply(height.divide(new BigDecimal(1000)).setScale(5)).
                        multiply(new BigDecimal(transomVO.getTransomNum())).setScale(0, BigDecimal.ROUND_HALF_UP));
            });
        }

    }

    /**
     * 计算订单表内五金信息需要计算的内容
     *
     * @param ironwareInfoVOS
     * @throws Exception
     */
    public void ironwareCalculate(List<IronwareInfoVO> ironwareInfoVOS) throws Exception {

        if (ironwareInfoVOS.size() > 0) {
            ironwareInfoVOS.stream().forEach(ironwareInfoVO -> {
                ironwareInfoVO.setTotalPrice(ironwareInfoVO.getPrice().
                        multiply(new BigDecimal(ironwareInfoVO.getIronwareNum()).setScale(0, BigDecimal.ROUND_HALF_UP)));
            });
        }

    }


    /**
     * 计算整个订单表需要计算的内容
     *
     * @param materialVO
     */
    public void orderCalculate(MaterialVO materialVO) {

        BigDecimal materialTotalPrice = new BigDecimal("0");

        for (MaterialInfoVO materialInfoVO : materialVO.getMaterials()) {
            materialTotalPrice = materialTotalPrice.add(materialInfoVO.getTotalPrice());
        }

        //累加天地横梁的价格
        if (materialVO.isHaveTransom && null != materialVO.getTransoms() && materialVO.getTransoms().size() > 0) {
            for (TransomVO transomVO : materialVO.getTransoms()) {
                materialTotalPrice = materialTotalPrice.add(transomVO.getTotalPrice());
            }
        }


        BigDecimal ironwareTotalPrice = new BigDecimal("0");


        for (IronwareInfoVO ironwareInfoVO : materialVO.getIronwares()) {

            ironwareTotalPrice = ironwareTotalPrice.add(ironwareInfoVO.getTotalPrice());

        }

        BigDecimal packageTotalPrice = NormalConf.BIG_PACKAGE_PRICE.
                multiply(new BigDecimal(materialVO.getBigPackageNum())).
                add(NormalConf.SIMPLE_PACKAGE_PRICE.multiply(new BigDecimal(materialVO.getSimplePackageNum()))).setScale(0, BigDecimal.ROUND_HALF_UP);


        materialVO.setIronTotalPrice(ironwareTotalPrice.add(packageTotalPrice).setScale(0, BigDecimal.ROUND_HALF_UP));

        materialVO.setMaterialTotalprice(materialTotalPrice.setScale(0, BigDecimal.ROUND_HALF_UP));

        materialVO.setOrderTotalPrice(materialTotalPrice.add(materialVO.getIronTotalPrice()).setScale(0, BigDecimal.ROUND_HALF_UP));

    }

    public Map<String, Object> toMap(MaterialVO materialVO) {

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("orderId", materialVO.getOrderId());
        dataMap.put("productType", ProductTypeEnums.getEnumByCode(materialVO.getProductType()));
        dataMap.put("customerNick", materialVO.getCustomerNick());
        dataMap.put("orderDate", materialVO.getOrderDate());
        dataMap.put("deliveryDate", materialVO.getDeliveryDate());
        dataMap.put("orderMaker", materialVO.getOrderMaker());
        dataMap.put("salesman", materialVO.getSalesman());
        dataMap.put("orderTotalPriceChina", OrderTools.convert(materialVO.getOrderTotalPrice().intValue()));
        if (materialVO.getIsClear()) {
            dataMap.put("isClear", "是");
        } else {
            dataMap.put("isClear", "不是");
        }

        MaterialInfoVO materialInfoVO = materialVO.getMaterials().get(0);
        dataMap.put("materialColor", MaterialColorEnums.getEnumByCode(materialInfoVO.getMaterialColor()));
        dataMap.put("materialType", MaterialTypeEnums.getEnumByCode(materialInfoVO.getMaterialType()));
        dataMap.put("handleType", HandleEnums.getNameByCode(materialInfoVO.getHandleType()));
        dataMap.put("glassColor", GlassColor.getNameByCode(materialInfoVO.getGlassColor()));
        dataMap.put("bigPackageNum", materialVO.getBigPackageNum());
        dataMap.put("bigPackagePrice", NormalConf.BIG_PACKAGE_PRICE.multiply(new BigDecimal(materialVO.getBigPackageNum()).setScale(2)));
        dataMap.put("simplePackageNum", materialVO.getSimplePackageNum());
        dataMap.put("simplePackagePrice", NormalConf.SIMPLE_PACKAGE_PRICE.multiply(new BigDecimal(materialVO.getSimplePackageNum()).setScale(2)));
        dataMap.put("ironTotalPrice", materialVO.getIronTotalPrice());
        dataMap.put("materialTotalPrice", "￥" + materialVO.getMaterialTotalprice());
        dataMap.put("orderRemark", materialVO.getRemark());
        dataMap.put("customerName", materialVO.getCustomerName());
        dataMap.put("customerPhoneNum", materialVO.getCustomerPhoneNum());
        dataMap.put("customerAddr", materialVO.getCustomerAddr());
        dataMap.put("orderTotalPrice", "￥" + materialVO.getOrderTotalPrice());
        dataMap.put("express", materialVO.getExpress());
        if (materialVO.getBigPackageNum() > 0 || materialVO.getSimplePackageNum() > 0) {

            int totalPackageNum = materialVO.getBigPackageNum() + materialVO.getSimplePackageNum();
            dataMap.put("packageType", "加强包装" + totalPackageNum + "个");
        } else {
            dataMap.put("packageType", "普通包装");
        }

        return dataMap;
    }


    public void fillData(HSSFSheet sheet, Map<String, List<MaterialInfoVO>> map, MaterialVO materialVO, List<IronwareInfoVO> ironwareInfoVOS, int addnum) {

        int IRONWARE_START_NUM = ExcelConf.IRONWARE_START_NUM + addnum;

        Map<Integer, String> letMap = new HashMap<>();
        letMap.put(0, "1");
        letMap.put(1, "2");
        letMap.put(2, "3");
        letMap.put(3, "4");
        letMap.put(4, "5");
        letMap.put(5, "6");
        letMap.put(6, "7");
        letMap.put(7, "8");
        letMap.put(8, "9");
        letMap.put(9, "10");

        int point1 = ExcelConf.MATERIAL_START_NUM;
        for (String key : map.keySet()) {

            HSSFRow titlerow = sheet.getRow(point1 - 3);

            HSSFCell titleCell = titlerow.getCell(0);

            titleCell.setCellValue(key);

            List<MaterialInfoVO> infoVOS = map.get(key);

            for (int i = 0; i < infoVOS.size(); i++) {

                MaterialInfoVO materialInfoVO0 = infoVOS.get(i);

                HSSFRow row = sheet.getRow(point1 - 1);

                for (int j = 0; j < row.getLastCellNum(); j++) {
                    HSSFCell cell = row.getCell(j);

                    if (j == 0) {
                        cell.setCellValue(letMap.get(i));
                    }

                    if (j == 1) {
                        cell.setCellValue(materialInfoVO0.getHeight() + "");
                    }

                    if (j == 2) {
                        cell.setCellValue(materialInfoVO0.getWidth() + "");
                    }

                    if (j == 3) {
                        cell.setCellValue(materialInfoVO0.getMaterialNum() + "");
                    }

                    if (j == 4) {
                        cell.setCellValue(materialInfoVO0.getHandlePlace());
                    }

                    if (j == 5) {
                        cell.setCellValue(materialInfoVO0.getHingeLocation());
                    }

                    if (j == 6) {
                        cell.setCellValue(materialInfoVO0.getDirection());
                    }

                    if (j == 7) {
                        cell.setCellValue(materialInfoVO0.getArea() + "");
                    }

                    if (j == 8) {
                        cell.setCellValue("￥" + materialInfoVO0.getPrice() + "");
                    }

                    if (j == 9) {
                        cell.setCellValue("￥" + materialInfoVO0.getTotalPrice() + "");
                    }

                    if (j == 10) {
                        cell.setCellValue(materialInfoVO0.getRemark() + "");
                    }
                }

                point1++;
            }

            point1 += 2;

        }

        if (materialVO.isHaveTransom && null != materialVO.getTransoms() && materialVO.getTransoms().size() > 0) {
            IRONWARE_START_NUM = 16 + addnum + materialVO.getTransoms().size() - 1;

            int TRANSOM_START_NUM = 13 + addnum;

            HSSFRow title = sheet.getRow(TRANSOM_START_NUM - 3);
            //放置横梁信息
            title.getCell(0).setCellValue(TransomTypeEnums.getEnumByCode(materialVO.getTransoms().get(0).getTransomType()));

            for (int i = 0; i < materialVO.getTransoms().size(); i++) {

                TransomVO infoVO = materialVO.getTransoms().get(i);

                HSSFRow row = sheet.getRow(TRANSOM_START_NUM - 1 + i);

                for (int j = 0; j < row.getLastCellNum(); j++) {

                    HSSFCell cell = row.getCell(j);

                    if (j == 0) {
                        cell.setCellValue(letMap.get(j));
                    }

                    if (j == 1) {
                        cell.setCellValue(MaterialColorEnums.getEnumByCode(infoVO.getTransomColor()));
                    }
                    if (j == 5) {
                        cell.setCellValue(infoVO.getHeight() + "");
                    }
                    if (j == 7) {
                        cell.setCellValue(infoVO.getTransomNum());
                    }
                    if (j == 8) {
                        cell.setCellValue(infoVO.getPrice() + "");
                    }
                    if (j == 9) {
                        cell.setCellValue(infoVO.getTotalPrice() + "");
                    }
                    if (j == 10) {
                        cell.setCellValue(infoVO.getRemark());
                    }

                }

            }

        }


        for (int l = 0; l < ironwareInfoVOS.size(); l++) {

            IronwareInfoVO ironwareInfoVO0 = ironwareInfoVOS.get(l);

            HSSFRow row = sheet.getRow(IRONWARE_START_NUM - 1 + l);

            for (int m = 0; m < row.getLastCellNum(); m++) {
                HSSFCell cell = row.getCell(m);

                if (m == 1) {
                    cell.setCellValue(ironwareInfoVO0.getIronwareName());
                }

                if (m == 4) {
                    cell.setCellValue("个");
                }

                if (m == 5) {
                    cell.setCellValue(IronwareColorEnums.getEnumByCode(ironwareInfoVO0.getIronwareColor()));
                }

                if (m == 6) {
                    cell.setCellValue(ironwareInfoVO0.getSpecification());
                }

                if (m == 7) {
                    cell.setCellValue(ironwareInfoVO0.getIronwareNum());
                }

                if (m == 8) {
                    cell.setCellValue("￥" + ironwareInfoVO0.getPrice() + "");
                }

                if (m == 9) {
                    cell.setCellValue("￥" + ironwareInfoVO0.getTotalPrice() + "");
                }

                if (m == 10) {
                    cell.setCellValue(ironwareInfoVO0.getRemark() + "");
                }
            }

        }

    }

    public void productFillData(HSSFSheet sheet, int addNum, Map<String, List<MaterialInfoVO>> map, MaterialVO materialVO, List<IronwareInfoVO> ironwareInfoVOS) {


        int IRONWARE_START_NUM = 16 + addNum;

        Map<Integer, String> letMap = new HashMap<>();
        letMap.put(0, "1");
        letMap.put(1, "2");
        letMap.put(2, "3");
        letMap.put(3, "4");
        letMap.put(4, "5");
        letMap.put(5, "6");
        letMap.put(6, "7");
        letMap.put(7, "8");
        letMap.put(8, "9");
        letMap.put(9, "10");

        int point1 = 10;
        int point2 = 13 + (addNum / 2);
        int serialNum = 1;
        for (String key : map.keySet()) {

            HSSFRow titlerow = sheet.getRow(point1 - 3);
            HSSFRow titlerow2 = sheet.getRow(point2 - 3);

            HSSFCell titleCell = titlerow.getCell(0);
            HSSFCell titleCell2 = titlerow2.getCell(0);

            titleCell.setCellValue("序号：" + serialNum + "=>" + key);
            titleCell2.setCellValue("订玻璃尺寸");

            serialNum++;

            List<MaterialInfoVO> infoVOS = map.get(key);

            for (int i = 0; i < infoVOS.size(); i++) {

                MaterialInfoVO materialInfoVO0 = infoVOS.get(i);

                HSSFRow row = sheet.getRow(point1 - 1);

                HSSFRow row2 = sheet.getRow(point2 - 1);

                for (int c = 0; c < row2.getLastCellNum(); c++) {
                    HSSFCell cell = row2.getCell(c);

                    if (c == 0) {
                        cell.setCellValue(letMap.get(i));
                    }
                    if (ProductTypeEnums.Complete.code == materialVO.getProductType() && c == 1) {
                        cell.setCellValue(materialInfoVO0.getGlassHeight() + "");
                    }
                    if (ProductTypeEnums.Complete.code == materialVO.getProductType() && c == 2) {
                        cell.setCellValue(materialInfoVO0.getGlassWidth() + "");
                    }
                    if (c == 3) {
                        cell.setCellValue(GlassColor.getNameByCode(materialInfoVO0.getGlassColor()));
                    }
                    if (ProductTypeEnums.Complete.code == materialVO.getProductType() && c == 4) {
                        cell.setCellValue(materialInfoVO0.getMaterialNum());
                    }
                    //下料详情先不写，螺丝数量和角码往下移
                    /*
                    if (c == 5) {
                        cell.setCellValue(materialInfoVO0.getMaterialDetail());
                    }

                    if (c == 6) {
                        cell.setCellValue(materialInfoVO0.getScrewNum() + "个");
                    }

                    if (c == 7) {
                        cell.setCellValue(CornerMaterialEnums.getEnumByCode(materialInfoVO0.getCornerMaterial()) + materialInfoVO0.getCornerNum() + "个");
                    }*/

                }

                point2++;

                for (int j = 0; j < row.getLastCellNum(); j++) {
                    HSSFCell cell = row.getCell(j);

                    if (j == 0) {
                        cell.setCellValue(letMap.get(i));
                    }

                    if (j == 1) {
                        cell.setCellValue(materialInfoVO0.getHeight() + "");
                    }

                    if (j == 2) {
                        cell.setCellValue(materialInfoVO0.getWidth() + "");
                    }

                    if (j == 3) {
                        cell.setCellValue(materialInfoVO0.getMaterialNum() + "");
                    }

                    if (j == 4) {
                        cell.setCellValue(materialInfoVO0.getHandlePlace());
                    }

                    if (j == 5) {
                        cell.setCellValue(materialInfoVO0.getHingeLocation());
                    }

                    if (j == 6) {
                        cell.setCellValue(materialInfoVO0.getDirection());
                    }

                    if (j == 7) {
                        cell.setCellValue("");
                    }

                    if (j == 8) {
                        cell.setCellValue(materialInfoVO0.getRemark());
                    }

                    /*if (j == 9) {
                        cell.setCellValue(materialInfoVO0.getTotalPrice() + "");
                    }

                    if (j == 10) {
                        cell.setCellValue(materialInfoVO0.getRemark() + "");
                    }*/
                }

                point1++;
            }

            point1 += 2;
            point2 += 2;

        }

        //填充天地横梁信息
        if (materialVO.isHaveTransom && null != materialVO.getTransoms() && materialVO.getTransoms().size() > 0) {
            //含有天地横梁信息
            //修改五金起始信息
            IRONWARE_START_NUM = 19 + addNum + materialVO.getTransoms().size() - 1;

            int TRANSOM_START_NUM = 16 + addNum;

            HSSFRow title = sheet.getRow(TRANSOM_START_NUM - 3);
            //放置横梁信息
            title.getCell(0).setCellValue(TransomTypeEnums.getEnumByCode(materialVO.getTransoms().get(0).getTransomType()));

            for (int i = 0; i < materialVO.getTransoms().size(); i++) {
                TransomVO infoVO = materialVO.getTransoms().get(i);

                HSSFRow row = sheet.getRow(TRANSOM_START_NUM - 1 + i);

                for (int j = 0; j < row.getLastCellNum(); j++) {
                    HSSFCell cell = row.getCell(j);

                    if (j == 0) {
                        cell.setCellValue(letMap.get(i));
                    }

                    if (j == 1) {
                        cell.setCellValue(MaterialColorEnums.getEnumByCode(infoVO.getTransomColor()));
                    }
                    if (j == 3) {
                        cell.setCellValue(infoVO.getHeight() + "");
                    }
                    if (j == 5) {
                        cell.setCellValue(infoVO.getTransomNum());
                    }
                    if (j == 7) {
                        cell.setCellValue(infoVO.getRemark());
                    }

                }

            }


        }

        //==========填充五金信息===========

        for (int d = 0; d < ironwareInfoVOS.size(); d++) {

            IronwareInfoVO infoVO = ironwareInfoVOS.get(d);

            HSSFRow row = sheet.getRow(IRONWARE_START_NUM - 1 + d);

            for (int e = 0; e < row.getLastCellNum(); e++) {

                HSSFCell cell = row.getCell(e);
                if (e == 1) {
                    cell.setCellValue(infoVO.getIronwareName());
                }
                if (e == 3) {
                    cell.setCellValue(infoVO.getIronwareNum());
                }
                if (e == 4) {
                    cell.setCellValue(infoVO.getUnit());
                }
                if (e == 5) {
                    cell.setCellValue(IronwareColorEnums.getEnumByCode(infoVO.getIronwareColor()));
                }
                if (e == 6) {
                    cell.setCellValue(infoVO.getSpecification());
                }
                if (e == 7) {
                    cell.setCellValue(infoVO.getRemark());
                }

            }

        }

    }

    public OrderInfo getOrderInfo(ProductVO materialVO) {

        OrderInfo info = new OrderInfo(materialVO.getIsClear(),
                ProductTypeEnums.getByCode(materialVO.getProductType()),
                materialVO.getCustomerNick(),
                materialVO.getCustomerName(),
                materialVO.getCustomerAddr(),
                materialVO.getCustomerPhoneNum(),
                materialVO.getBigPackageNum(),
                materialVO.getSimplePackageNum(),
                materialVO.getOrderDate(),
                materialVO.getDeliveryDate(),
                materialVO.getOrderId(),
                materialVO.getSalesman(),
                materialVO.getOrderMaker(),
                materialVO.getOrderTotalPrice(),
                materialVO.getExpress());

        return info;

    }

    public void saveMaterialInfoList(MaterialInfoMapper mapper, String uid, List<MaterialInfoVO> materialInfoVOS) {

        for (MaterialInfoVO materialInfoVO : materialInfoVOS) {

            MaterialInfo info = new MaterialInfo(uid, MaterialColorEnums.getByCode(materialInfoVO.getGlassColor()),
                    MaterialTypeEnums.getByCode(materialInfoVO.getMaterialType()),
                    HandleEnums.getByCode(materialInfoVO.getHandleType()),
                    materialInfoVO.getHingeLocation(), GlassColor.getByCode(materialInfoVO.getGlassColor()),
                    materialInfoVO.getGlassHeight(), CornerMaterialEnums.getByCode(materialInfoVO.getCornerMaterial()), materialInfoVO.getGlassWidth(),
                    materialInfoVO.getHeight(), materialInfoVO.getWidth(),
                    materialInfoVO.getMaterialHeight(), materialInfoVO.getMaterialWidth(),
                    materialInfoVO.getMaterialNum(), materialInfoVO.getHandlePlace(),
                    materialInfoVO.getDirection(),
                    materialInfoVO.getMaterialDetail(), materialInfoVO.getRemark(),
                    materialInfoVO.getPrice(), materialInfoVO.getArea(), materialInfoVO.getTotalPrice());
            mapper.insert(info);

        }


    }

    public void saveIronwareInfoList(IronwareInfoMapper mapper, List<IronwareInfoVO> ironwareInfoVOS, String uid) {

        for (IronwareInfoVO ironwareInfoVO : ironwareInfoVOS) {

            IronwareInfo info = new IronwareInfo(uid, ironwareInfoVO.getIronwareName(), ironwareInfoVO.getUnit(), IronwareColorEnums.getEnumByCode(ironwareInfoVO.getIronwareColor()),
                    ironwareInfoVO.getSpecification(), ironwareInfoVO.getIronwareNum(), ironwareInfoVO.getPrice(),
                    ironwareInfoVO.getRemark(), ironwareInfoVO.getTotalPrice());

            mapper.insert(info);
        }

    }

    public void saveTransomInfoList(TransomMapper mapper, List<TransomVO> transomVOS, String uid) {

        for (TransomVO transomVO : transomVOS) {

            TransomInfo info = new TransomInfo(uid, TransomTypeEnums.getByCode(transomVO.getTransomType()),
                    MaterialColorEnums.getByCode(transomVO.getTransomColor()), transomVO.getHeight(),
                    transomVO.getTransomNum(), transomVO.getPrice(), transomVO.getTotalPrice(), transomVO.getRemark());

            mapper.insert(info);
        }

    }

    public void validateCell(HSSFRow row) {

        if (row.getPhysicalNumberOfCells() < 1) {
            for (int i = 0; i < 10; i++) {

                HSSFCell cell = row.createCell(i);
                cell.setCellValue("");


            }
        }

    }

    /**
     * @param args
     * add by zxx ,Nov 29, 2008
     */
    private static final char[] data = new char[]{'零', '壹', '贰', '叁', '肆',
            '伍', '陆', '柒', '捌', '玖'};

    private static final char[] units = new char[]{'元', '拾', '佰', '仟', '万',
            '拾', '佰', '仟', '亿'};

    public static String convert(int money) {
        StringBuffer sbf = new StringBuffer();
        int unit = 0;
        while (money != 0) {
            sbf.insert(0, units[unit++]);
            int number = money % 10;
            sbf.insert(0, data[number]);
            money /= 10;
        }
        return sbf.toString();
    }


    public MaterialVO revertToMaterialVO(OrderInfo orderInfo, List<MaterialInfo> materialInfos, List<IronwareInfo> ironwareInfos) {

        MaterialVO materialVO = new MaterialVO();
        if (null == orderInfo.getIsClear()) {
            materialVO.setIsClear(true);
        } else {
            materialVO.setIsClear(orderInfo.getIsClear());
        }
        materialVO.setIsClear(orderInfo.getIsClear());
        materialVO.setProductType(orderInfo.getProductType().code);
        materialVO.setCustomerName(orderInfo.getCustomerName());
        materialVO.setCustomerNick(orderInfo.getCustomerNick());
        materialVO.setCustomerAddr(orderInfo.getCustomerAddr());
        materialVO.setCustomerPhoneNum(orderInfo.getCustomerPhoneNum());
        materialVO.setExpress(orderInfo.getExpress());
        materialVO.setBigPackageNum(orderInfo.getBigPackageNum());
        materialVO.setSimplePackageNum(orderInfo.getSimplePackageNum());
        materialVO.setOrderDate(orderInfo.getOrderDate());
        materialVO.setDeliveryDate(orderInfo.getDeliveryDate());
        materialVO.setOrderId(orderInfo.getOrderId());
        materialVO.setSalesman(orderInfo.getSalesman());
        materialVO.setOrderMaker(orderInfo.getOrderMaker());
        materialVO.setOrderTotalPrice(orderInfo.getTotalPrice());

        List<MaterialInfoVO> materialInfoVOS = new ArrayList<>();
        for (MaterialInfo materialInfo : materialInfos) {
            MaterialInfoVO materialInfoVO = new MaterialInfoVO();
            if (null == materialInfo.getMaterialColor()) {
                materialInfoVO.setMaterialColor(1);
            } else {
                materialInfoVO.setMaterialColor(materialInfo.getMaterialColor().code);
            }

            if (null == materialInfo.getCornerMaterial()) {
                materialInfoVO.setMaterialType(1001);
            } else {
                materialInfoVO.setMaterialType(materialInfo.getMaterialType().code);
            }

            if (null == materialInfo.getHandleType()) {
                materialInfoVO.setHandleType(0);
            } else {
                materialInfoVO.setHandleType(materialInfo.getHandleType().code);
            }

            materialInfoVO.setHingeLocation(materialInfo.getHingeLocation());

            if (null == materialInfo.getGlassColor()) {
                materialInfoVO.setGlassColor(0);
            } else {
                materialInfoVO.setGlassColor(materialInfo.getGlassColor().code);
            }


            if (null == materialInfo.getCornerMaterial()) {
                materialInfoVO.setCornerMaterial(0);
            } else {
                materialInfoVO.setCornerMaterial(materialInfo.getCornerMaterial().code);
            }
            materialInfoVO.setGlassHeight(materialInfo.getGlassHeight());
            materialInfoVO.setGlassWidth(materialInfo.getGlassWidth());
            materialInfoVO.setHeight(materialInfo.getHeight());
            materialInfoVO.setWidth(materialInfo.getWidth());
            materialInfoVO.setMaterialWidth(materialInfo.getMaterialWidth());
            materialInfoVO.setMaterialHeight(materialInfo.getMaterialHeight());
            materialInfoVO.setMaterialNum(materialInfo.getMaterialNum());
            materialInfoVO.setHandlePlace(materialInfo.getHandlePlace());
            materialInfoVO.setDirection(materialInfo.getDirection());
            materialInfoVO.setMaterialDetail(materialInfo.getMaterialDetail());
            materialInfoVO.setRemark(materialInfo.getRemark());
            materialInfoVO.setPrice(materialInfo.getPrice());
            materialInfoVO.setArea(materialInfo.getArea());
            materialInfoVO.setTotalPrice(materialInfo.getTotalPrice());
            materialInfoVOS.add(materialInfoVO);
        }
        materialVO.setMaterials(materialInfoVOS);

        List<IronwareInfoVO> ironwareInfoVOS = new ArrayList<>();

        for (IronwareInfo ironwareInfo : ironwareInfos) {

            IronwareInfoVO ironwareInfoVO = new IronwareInfoVO();
            ironwareInfoVO.setIronwareName(ironwareInfo.getIronwareName());
            ironwareInfoVO.setUnit(ironwareInfo.getUnit());
            if (null == ironwareInfo.getIronwareColor()) {
                ironwareInfoVO.setIronwareColor(IronwareColorEnums.NOCOLOR.code);
            } else {
                ironwareInfoVO.setIronwareColor(IronwareColorEnums.getEnumByName(ironwareInfo.getIronwareColor()).code);
            }
            ironwareInfoVO.setSpecification(ironwareInfo.getSpecification());
            ironwareInfoVO.setPrice(ironwareInfo.getPrice());
            ironwareInfoVO.setRemark(ironwareInfo.getRemark());
            ironwareInfoVO.setTotalPrice(ironwareInfo.getTotalPrice());

            ironwareInfoVOS.add(ironwareInfoVO);

        }
        materialVO.setIronwares(ironwareInfoVOS);


        return materialVO;


    }

    public void saveCustomer(String adminUid, ProductVO productVO, PersonFeignClient personFeignClient) {
        //保存客户信息
        CustomerVO customerVO = new CustomerVO();
        customerVO.setAdminUid(adminUid);
        customerVO.setCustomerResource(CustomerResourceEnums.ORDER.code);
        customerVO.setCunstomerName(productVO.getCustomerName());
        customerVO.setCustomerAddr(productVO.getCustomerAddr());
        customerVO.setCutomerPhone(productVO.getCustomerPhoneNum());
        customerVO.setSalesman(productVO.getSalesman());
        //改成发送消息
        rabbitTemplate.convertAndSend(SysConf.EXCHANGE_DIRECT, SysConf.BINGO_WEB, JsonUtils.objectToJson(customerVO));
        //personFeignClient.saveCustomerByOrder(customerVO);
    }
}
