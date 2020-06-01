package com.bingo.erp.xo.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.db.sql.Order;
import com.bingo.erp.base.enums.*;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.fatocry.material.MaterialCalculateFactory;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.IronwareInfo;
import com.bingo.erp.commons.entity.MaterialInfo;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.commons.entity.Product;
import com.bingo.erp.utils.DateUtils;
import com.bingo.erp.xo.enums.MaterialFactoryEnum;
import com.bingo.erp.xo.global.ExcelConf;
import com.bingo.erp.xo.global.NormalConf;
import com.bingo.erp.xo.mapper.IronwareInfoMapper;
import com.bingo.erp.xo.mapper.MaterialInfoMapper;
import com.bingo.erp.xo.mapper.OrderInfoMapper;
import com.bingo.erp.xo.mapper.ProductMapper;
import com.bingo.erp.xo.service.OrderService;
import com.bingo.erp.xo.vo.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.jxls.transformer.XLSTransformer;
import net.sf.jxls.util.Util;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class OrderServiceImpl extends SuperServiceImpl<OrderInfoMapper, OrderInfo> implements OrderService {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private MaterialInfoMapper materialInfoMapper;

    @Resource
    private IronwareInfoMapper ironwareInfoMapper;


    @Override
    public List<IndexOrderVO> getIndexOrderInfo() {

        List<IndexOrderVO> result = new ArrayList<>();
        List<OrderInfo> infos = orderInfoMapper.getIndexOrderInfo();

        for (OrderInfo info : infos) {
            IndexOrderVO vo = new IndexOrderVO(info.getOrderId(), info.getTotalPrice(), info.getSalesman(), info.getStatus());
            result.add(vo);
        }

        return result;
    }

    @Override
    public List<String> saveOrder(MaterialVO materialVO) throws Exception {

        log.info("===============方法开始，参数信息：excel文件夹：" + ExcelConf.NEW_FILE_DICT);

        List<String> result = new ArrayList<>();

        String suffix = DateUtils.formateDate(new Date(), DateUtils.DAYFORMAT_STRING) + RandomUtil.randomNumbers(6) + ExcelConf.FILE_SUFFIX;

        String fileName = "订单" + suffix;

        result.add(fileName);


        //确定表格结构
        if (null == materialVO.getMaterials() || materialVO.getMaterials().size() <= 0) {
            throw new MessageException("没有填写材玻信息哦~请填写");
        }

        if (null == materialVO.getIronwares()) {
            materialVO.setIronwares(new ArrayList<>());
        }

        /*if (!materialValidate(materialVO.getMaterials())) {
            throw new MessageException("要保持料型号颜色、拉手类型和玻璃颜色一致哦~");
        }*/

        int materialNum = materialVO.getMaterials().size() - 1;

        int ironwareNum = materialVO.getIronwares().size() - 1;

        //根据数量扩充表格

        //计算数据

        ironwareCalculate(materialVO.getIronwares());
        materialCalculate(materialVO.getMaterials());
        orderCalculate(materialVO);

        try {

            POIFSFileSystem fs = new POIFSFileSystem(new File(ExcelConf.SRC_FILE_URL + ExcelConf.ORDER_FILENAME));

            HSSFWorkbook wb = new HSSFWorkbook(fs);

            HSSFSheet sheet = wb.getSheetAt(0);

            Map<String, List<MaterialInfoVO>> map = materialsToMap(materialVO.getMaterials());

            int addnum = extensionExcel(sheet, map, ironwareNum);

            //填充料玻、五金数据
            fillData(sheet, map, materialVO.getIronwares(), addnum);

            File newFile = new File(ExcelConf.NEW_FILE_DICT + fileName);

            wb.write(newFile);

            Map<String, Object> dataMap = toMap(materialVO);

            XLSTransformer transformer = new XLSTransformer();
            transformer.transformXLS(ExcelConf.NEW_FILE_DICT + fileName, dataMap, ExcelConf.NEW_FILE_DICT + fileName);

            /**
             =========================== 生产单制作 ================================
             */

            String productFileName = "生产单" + suffix;

            File productFile = new File(ExcelConf.NEW_FILE_DICT + productFileName);

            POIFSFileSystem productFs = new POIFSFileSystem(new File(ExcelConf.SRC_FILE_URL + ExcelConf.PRODUCT_ORDER_FILENAME));

            HSSFWorkbook productWb = new HSSFWorkbook(productFs);

            HSSFSheet productSheet = productWb.getSheetAt(0);

            int addnum1 = productExtensionExcel(productSheet, map, materialNum, ironwareNum);

            productFillData(productSheet, addnum1, map, materialVO.getMaterials(), materialVO.getIronwares());

            productWb.write(productFile);

            Thread.sleep(1000l);

            transformer.transformXLS(ExcelConf.NEW_FILE_DICT + productFileName, dataMap, ExcelConf.NEW_FILE_DICT + productFileName);

            result.add(productFileName);

            /**
             * ==========================================存入数据库 ======================================
             */

            OrderInfo orderInfo = getOrderInfo(materialVO);

            orderInfoMapper.insert(orderInfo);

            saveMaterialInfoList(materialInfoMapper, orderInfo.getUid(), materialVO.getMaterials());

            saveIronwareInfoList(ironwareInfoMapper, materialVO.getIronwares(), orderInfo.getUid());

            log.info("主键id" + orderInfo.getUid());

            return result;

        } catch (Exception e) {

            e.printStackTrace();
            throw new MessageException("生成表格失败！");
        }


    }


    private boolean materialValidate(List<MaterialInfoVO> materialInfoVOS) {

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

    private Map<String, List<MaterialInfoVO>> materialsToMap(List<MaterialInfoVO> materialInfoVOS) {

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

    private int extensionExcel(HSSFSheet sheet, Map<String, List<MaterialInfoVO>> map, int ironwareNum) {


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

        int ironwareStartNum = ExcelConf.IRONWARE_START_NUM + addNum;

        if (ironwareNum > 0) {
            sheet.shiftRows(ironwareStartNum, sheet.getLastRowNum(), ironwareNum, true, false);

            for (int i = 0; i < ironwareNum; i++) {
                Util.copyRow(sheet, sheet.getRow(ironwareStartNum - 1), sheet.getRow(ironwareStartNum + i));
            }
        }

        return addNum;


    }

    private int productExtensionExcel(HSSFSheet sheet, Map<String, List<MaterialInfoVO>> map, int materialNum, int ironwareNum) {


        int addNum = (map.keySet().size() - 1) * 2;

        for (String key : map.keySet()) {

            addNum += map.get(key).size();

        }

        //addNum = addNum ;

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

        if (ironwareNum > 0) {
            int IRONWARE_START_NUM = 16 + addNum * 2;

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
    private void materialCalculate(List<MaterialInfoVO> materialInfoVOS) throws Exception {


        for (MaterialInfoVO materialInfoVO : materialInfoVOS) {
            //todo 计算玻璃和料型

            //获取产品ID
            int materialType = materialInfoVO.getMaterialType();

            MaterialCalculateFactory factory = (MaterialCalculateFactory) MaterialFactoryEnum.getFactoryClass(Integer.valueOf(materialType)).newInstance();
            MaterialCalculateResultVO vo = (MaterialCalculateResultVO) factory.calculate(materialInfoVO);

            materialInfoVO.setMaterialHeight(vo.getMaterialHeight());
            materialInfoVO.setMaterialWidth(vo.getMaterialWidth());

            materialInfoVO.setGlassHeight(vo.getGlassHeight());
            materialInfoVO.setGlassWidth(vo.getGlassWidth());

            materialInfoVO.setMaterialDetail(vo.getMaterialDetail());

            BigDecimal singleArea = materialInfoVO.getHeight().
                    multiply(materialInfoVO.getWidth()).divide(NormalConf.divideNum).setScale(2, BigDecimal.ROUND_HALF_UP);

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
                            multiply(new BigDecimal(materialInfoVO.getMaterialNum())).setScale(2, BigDecimal.ROUND_CEILING)
            );

            materialInfoVO.setTotalPrice(materialInfoVO.getArea().multiply(materialInfoVO.getPrice()).setScale(0, BigDecimal.ROUND_HALF_UP));

        }


    }

    /**
     * 计算订单表内五金信息需要计算的内容
     *
     * @param ironwareInfoVOS
     * @throws Exception
     */
    private void ironwareCalculate(List<IronwareInfoVO> ironwareInfoVOS) throws Exception {

        if (ironwareInfoVOS.size() > 0) {
            ironwareInfoVOS.stream().forEach(ironwareInfoVO -> {
                ironwareInfoVO.setTotalPrice(ironwareInfoVO.getPrice().
                        multiply(new BigDecimal(ironwareInfoVO.getIronwareNum()).setScale(2)));
            });
        }

    }

    /**
     * 计算整个订单表需要计算的内容
     *
     * @param materialVO
     */
    private void orderCalculate(MaterialVO materialVO) {

        BigDecimal materialTotalPrice = new BigDecimal("0");


        for (MaterialInfoVO materialInfoVO : materialVO.getMaterials()) {

            materialTotalPrice = materialTotalPrice.add(materialInfoVO.getTotalPrice());

        }

        BigDecimal ironwareTotalPrice = new BigDecimal("0");


        for (IronwareInfoVO ironwareInfoVO : materialVO.getIronwares()) {

            ironwareTotalPrice = ironwareTotalPrice.add(ironwareInfoVO.getTotalPrice());

        }

        BigDecimal packageTotalPrice = NormalConf.BIG_PACKAGE_PRICE.
                multiply(new BigDecimal(materialVO.getBigPackageNum())).
                add(NormalConf.SIMPLE_PACKAGE_PRICE.multiply(new BigDecimal(materialVO.getSimplePackageNum())));


        materialVO.setIronTotalPrice(ironwareTotalPrice.add(packageTotalPrice));

        materialVO.setMaterialTotalprice(materialTotalPrice);

        materialVO.setOrderTotalPrice(materialTotalPrice.add(materialVO.getIronTotalPrice()));

    }

    private Map<String, Object> toMap(MaterialVO materialVO) {

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("orderId", materialVO.getOrderId());
        dataMap.put("productType", ProductTypeEnums.getEnumByCode(materialVO.getProductType()));
        dataMap.put("customerNick", materialVO.getCustomerNick());
        dataMap.put("orderDate", materialVO.getOrderDate());
        dataMap.put("deliveryDate", materialVO.getDeliveryDate());
        dataMap.put("orderMaker", materialVO.getOrderMaker());
        dataMap.put("salesman", materialVO.getSalesman());
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
        dataMap.put("materialTotalPrice", materialVO.getMaterialTotalprice());
        dataMap.put("orderRemark", materialVO.getRemark());
        dataMap.put("customerName", materialVO.getCustomerName());
        dataMap.put("customerPhoneNum", materialVO.getCustomerPhoneNum());
        dataMap.put("customerAddr", materialVO.getCustomerAddr());
        dataMap.put("orderTotalPrice", materialVO.getOrderTotalPrice());
        dataMap.put("express", materialVO.getExpress());
        if (materialVO.getBigPackageNum() > 0 || materialVO.getSimplePackageNum() > 0) {

            int totalPackageNum = materialVO.getBigPackageNum() + materialVO.getSimplePackageNum();
            dataMap.put("packageType", "加强包装" + totalPackageNum + "个");
        } else {
            dataMap.put("packageType", "普通包装");
        }

        return dataMap;
    }


    private void fillData(HSSFSheet sheet, Map<String, List<MaterialInfoVO>> map, List<IronwareInfoVO> ironwareInfoVOS, int addnum) {

        Map<Integer, String> letMap = new HashMap<>();
        letMap.put(0, "A");
        letMap.put(1, "B");
        letMap.put(2, "C");
        letMap.put(3, "D");
        letMap.put(4, "E");
        letMap.put(5, "F");
        letMap.put(6, "G");
        letMap.put(7, "H");
        letMap.put(8, "I");
        letMap.put(9, "J");

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
                        cell.setCellValue(HandleEnums.getNameByCode(materialInfoVO0.getHandleType()));
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
                        cell.setCellValue(materialInfoVO0.getPrice() + "");
                    }

                    if (j == 9) {
                        cell.setCellValue(materialInfoVO0.getTotalPrice() + "");
                    }

                    if (j == 10) {
                        cell.setCellValue(materialInfoVO0.getRemark() + "");
                    }
                }

                point1++;
            }

            point1 += 2;

        }

        for (int l = 0; l < ironwareInfoVOS.size(); l++) {

            IronwareInfoVO ironwareInfoVO0 = ironwareInfoVOS.get(l);

            HSSFRow row = sheet.getRow(ExcelConf.IRONWARE_START_NUM - 1 + addnum + l);

            for (int m = 0; m < row.getLastCellNum(); m++) {
                HSSFCell cell = row.getCell(m);

                if (m == 1) {
                    cell.setCellValue(ironwareInfoVO0.getIronwareName());
                }

                if (m == 4) {
                    cell.setCellValue("个");
                }

                if (m == 5) {
                    cell.setCellValue(ironwareInfoVO0.getIronwareColor());
                }

                if (m == 6) {
                    cell.setCellValue(ironwareInfoVO0.getSpecification());
                }

                if (m == 7) {
                    cell.setCellValue(ironwareInfoVO0.getIronwareNum());
                }

                if (m == 8) {
                    cell.setCellValue(ironwareInfoVO0.getPrice() + "");
                }

                if (m == 9) {
                    cell.setCellValue(ironwareInfoVO0.getTotalPrice() + "");
                }

                if (m == 10) {
                    cell.setCellValue(ironwareInfoVO0.getRemark() + "");
                }
            }

        }

    }

    private void productFillData(HSSFSheet sheet, int addNum, Map<String, List<MaterialInfoVO>> map, List<MaterialInfoVO> materialInfoVOS, List<IronwareInfoVO> ironwareInfoVOS) {

        int IRONWARE_START_NUM = 16 + addNum;

        Map<Integer, String> letMap = new HashMap<>();
        letMap.put(0, "A");
        letMap.put(1, "B");
        letMap.put(2, "C");
        letMap.put(3, "D");
        letMap.put(4, "E");
        letMap.put(5, "F");
        letMap.put(6, "G");
        letMap.put(7, "H");
        letMap.put(8, "I");
        letMap.put(9, "J");

        int point1 = 10;
        int point2 = 13 + (addNum / 2);
        int serialNum = 1;
        for (String key : map.keySet()) {

            HSSFRow titlerow = sheet.getRow(point1 - 3);
            HSSFRow titlerow2 = sheet.getRow(point2 - 3);

            HSSFCell titleCell = titlerow.getCell(0);
            HSSFCell titleCell2 = titlerow2.getCell(0);

            titleCell.setCellValue("序号：" + serialNum + "=>" + key);
            titleCell2.setCellValue("序号：" + serialNum + "=>下料信息（" + key + ")");

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
                    if (c == 1) {
                        cell.setCellValue(materialInfoVO0.getGlassHeight() + "");
                    }
                    if (c == 2) {
                        cell.setCellValue(materialInfoVO0.getGlassWidth() + "");
                    }
                    if (c == 3) {
                        cell.setCellValue(GlassColor.getNameByCode(materialInfoVO0.getGlassColor()));
                    }
                    if (c == 4) {
                        cell.setCellValue(materialInfoVO0.getMaterialNum());
                    }
                    if (c == 5) {
                        cell.setCellValue(materialInfoVO0.getMaterialDetail());
                    }

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

    private OrderInfo getOrderInfo(MaterialVO materialVO) {

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

    private void saveMaterialInfoList(MaterialInfoMapper mapper, String uid, List<MaterialInfoVO> materialInfoVOS) {

        for (MaterialInfoVO materialInfoVO : materialInfoVOS) {

            MaterialInfo info = new MaterialInfo(uid, MaterialColorEnums.getByCode(materialInfoVO.getGlassColor()),
                    MaterialTypeEnums.getByCode(materialInfoVO.getMaterialType()),
                    HandleEnums.getByCode(materialInfoVO.getHandleType()),
                    materialInfoVO.getHingeLocation(), GlassColor.getByCode(materialInfoVO.getGlassColor()),
                    materialInfoVO.getGlassHeight(), materialInfoVO.getGlassWidth(),
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

    private void validateCell(HSSFRow row) {

        if (row.getPhysicalNumberOfCells() < 1) {
            for (int i = 0; i < 10; i++) {

                HSSFCell cell = row.createCell(i);
                cell.setCellValue("");


            }
        }

    }
}
