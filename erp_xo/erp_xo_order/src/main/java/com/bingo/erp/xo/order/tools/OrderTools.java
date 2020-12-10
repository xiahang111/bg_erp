package com.bingo.erp.xo.order.tools;

import com.bingo.erp.base.enums.*;
import com.bingo.erp.base.fatocry.material.MaterialCalculateFactory;
import com.bingo.erp.base.vo.CustomerVO;
import com.bingo.erp.commons.entity.*;
import com.bingo.erp.utils.JsonUtils;
import com.bingo.erp.utils.RedisUtil;
import com.bingo.erp.xo.order.enums.MaterialFactoryEnum;
import com.bingo.erp.xo.order.global.ExcelConf;
import com.bingo.erp.xo.order.global.NormalConf;
import com.bingo.erp.xo.order.global.RedisConf;
import com.bingo.erp.xo.order.global.SysConf;
import com.bingo.erp.xo.order.mapper.IronwareInfoMapper;
import com.bingo.erp.xo.order.mapper.MaterialInfoMapper;
import com.bingo.erp.xo.order.mapper.TransomMapper;
import com.bingo.erp.xo.order.service.AdminService;
import com.bingo.erp.xo.order.service.RoleService;
import com.bingo.erp.xo.order.vo.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.jxls.util.Util;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 订单方法集中类，先放在这里，以后再重构订单的生成方法
 */
@Slf4j
@Component
public class OrderTools {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Autowired
    RedisUtil redisUtil;

    @Resource
    private RoleService roleService;

    @Resource
    private AdminService adminService;

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
                    GlassColor.getNameByCode(infoVO.getGlassColor()) + "玻璃" + (infoVO.isHaveBar==1?"":"(不含静音条)");

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

                for (Integer j = 0; j < add; j++) {
                    j = addIfNull(sheet,rowNum,j);
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
                        i = addIfNull(sheet,point,i);
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

    private Integer addIfNull(HSSFSheet sheet,Integer point,Integer i){
        if(null == sheet.getRow(point + i)){
            i++;
            i = addIfNull(sheet,point,i);
        }

        return i;

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
                /*

                case 7004:
                    minArea = new BigDecimal("0.8");
                    break;
                case 5003:
                    minArea = new BigDecimal("0.8");
                    break;
                case 6001:
                    minArea = new BigDecimal("0.8");
                    break;*/
                case 4001:
                    minArea = new BigDecimal("0.8");
                    break;
                case 5003:
                    minArea = new BigDecimal("0.8");
                    break;
                case 2001:
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

    /**
     * 给半成品层板灯添加配件
     * @return
     */
    public List<IronwareInfoVO> getCBDIronByNotComplete(int num,int productType) {

        List<IronwareInfoVO> results = new ArrayList<>();
        if (productType == ProductTypeEnums.NotComplete.code){
            IronwareInfoVO ironwareInfoVO1 = new IronwareInfoVO();
            ironwareInfoVO1.setIronwareNum(4 * num);
            ironwareInfoVO1.setIronwareName("层板角码");
            ironwareInfoVO1.setUnit("个");

            IronwareInfoVO ironwareInfoVO4 = new IronwareInfoVO();
            ironwareInfoVO4.setIronwareNum(8 * num);
            ironwareInfoVO4.setIronwareName("角码螺丝");
            ironwareInfoVO4.setUnit("个");
            results.add(ironwareInfoVO1);
            results.add(ironwareInfoVO4);
        }



        IronwareInfoVO ironwareInfoVO2 = new IronwareInfoVO();
        ironwareInfoVO2.setIronwareNum(4 * num);
        ironwareInfoVO2.setIronwareName("层板托");
        ironwareInfoVO2.setUnit("个");

        IronwareInfoVO ironwareInfoVO3 = new IronwareInfoVO();
        ironwareInfoVO3.setIronwareNum(4 * num);
        ironwareInfoVO3.setIronwareName("层板托螺丝");
        ironwareInfoVO3.setUnit("个");


        results.add(ironwareInfoVO2);
        results.add(ironwareInfoVO3);

        return results;

    }

    public List<IronwareInfoVO> getIronByHeight(List<TransomVO> transomVOS) {

        List<IronwareInfoVO> results = new ArrayList<>();


        int ls60 = 0;
        int ls30 = 0;

        for (TransomVO transomVO:transomVOS) {
            if (transomVO.getHeight().compareTo(new BigDecimal("500")) < 0) {
                ls60 += 1;
                ls30 += 3;
            } else {
                ls30 += 6;

            }
        }

        if (ls60 > 0){
            IronwareInfoVO ironwareInfoVO1 = new IronwareInfoVO();
            ironwareInfoVO1.setIronwareNum(ls60);
            ironwareInfoVO1.setIronwareName("60螺丝");
            ironwareInfoVO1.setUnit("个");
            results.add(ironwareInfoVO1);
        }
        if (ls30 > 0){
            IronwareInfoVO ironwareInfoVO2 = new IronwareInfoVO();
            ironwareInfoVO2.setIronwareNum(ls30);
            ironwareInfoVO2.setIronwareName("30螺丝");
            ironwareInfoVO2.setUnit("个");
            results.add(ironwareInfoVO2);
        }
        return results;

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
        } else {

        }

    }

    /**
     * 配件填充计算
     */
    public void mountingCalculate(MaterialVO materialVO){

        List<MaterialInfoVO> materialInfoVOS = materialVO.getMaterials();

        //如果是半成品则玻璃颜色都改为无
        if(materialVO.getProductType() == ProductTypeEnums.NotComplete.code){
            for (MaterialInfoVO materialInfoVO:materialInfoVOS) {
                materialInfoVO.setGlassColor(GlassColor.NOGLASS.code);
            }
        }
        //50斜边计算规则
        Map<Integer ,Integer> xb50Map = new HashMap<>();
        if(materialVO.getProductType() == ProductTypeEnums.NotComplete.code && null != materialInfoVOS && materialInfoVOS.size() > 0 ){

            for (MaterialInfoVO materialInfoVO:materialInfoVOS) {

                //根据颜色来set值
                if (materialInfoVO.materialType == MaterialTypeEnums.XB50.code){
                    int materialColor = materialInfoVO.getMaterialColor();
                    if(null == xb50Map.get(materialColor) || xb50Map.get(materialColor) <= 0 ){
                        xb50Map.put(materialColor,materialInfoVO.getMaterialNum());
                    }else {
                        xb50Map.put(materialColor,xb50Map.get(materialColor) + materialInfoVO.getMaterialNum());
                    }
                }
            }

            int black = xb50Map.getOrDefault(MaterialColorEnums.LSH.code,0);
            int gray = xb50Map.getOrDefault(MaterialColorEnums.SSH.code,0) +
                    xb50Map.getOrDefault(MaterialColorEnums.LSHUI.code,0) +
                    xb50Map.getOrDefault(MaterialColorEnums.LMH.code,0) ;

            if(black > 0 ){
                IronwareInfoVO ironwareInfoVO = new IronwareInfoVO();
                ironwareInfoVO.setSpecification("4 x 20");
                ironwareInfoVO.setIronwareColor(IronwareColorEnums.BLACK.code);
                ironwareInfoVO.setIronwareNum(8 * black);
                ironwareInfoVO.setIronwareName("50斜边专用螺丝");
                materialVO.getIronwares().add(ironwareInfoVO);
            }
            if(gray > 0 ){
                IronwareInfoVO ironwareInfoVO = new IronwareInfoVO();
                ironwareInfoVO.setSpecification("4 x 20");
                ironwareInfoVO.setIronwareColor(IronwareColorEnums.GRAY.code);
                ironwareInfoVO.setIronwareNum(8 * gray);
                ironwareInfoVO.setIronwareName("50斜边专用螺丝");
                materialVO.getIronwares().add(ironwareInfoVO);
            }

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

            if (null != ironwareInfoVO.getTotalPrice()) {
                ironwareTotalPrice = ironwareTotalPrice.add(ironwareInfoVO.getTotalPrice());
            }
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

    private Integer getRowIfNull(HSSFSheet sheet,Integer point){
        if(null == sheet.getRow(point - 1)){
            point ++;
            point = getRowIfNull(sheet,point);
        }
        return point;
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

                point1 = getRowIfNull(sheet,point1);
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
                        cell.setCellValue(materialInfoVO0.getHandlePlace() + "");
                    }

                    if (j == 5) {
                        cell.setCellValue(materialInfoVO0.getHingeLocation() + "");
                    }

                    if (j == 6) {
                        cell.setCellValue(materialInfoVO0.getDirection() + "");
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
                        cell.setCellValue(materialInfoVO0.getRemark());
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
            //放置横梁信息 TransomTypeEnums.getEnumByCode(materialVO.getTransoms().get(0).getTransomType())
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
                        cell.setCellValue(infoVO.getTransomNum() + "");
                    }
                    if (j == 8) {
                        cell.setCellValue(infoVO.getPrice() + "");
                    }
                    if (j == 9) {
                        cell.setCellValue(infoVO.getTotalPrice() + "");
                    }
                    if (j == 10) {
                        cell.setCellValue(infoVO.getRemark() + "");
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
                    cell.setCellValue(ironwareInfoVO0.getSpecification() + "");
                }

                if (m == 7) {
                    cell.setCellValue(ironwareInfoVO0.getIronwareNum() + "");
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
                    if (CBDOrderTools.isNeedGlass(key) && ProductTypeEnums.Complete.code == materialVO.getProductType() && c == 1) {
                        cell.setCellValue(materialInfoVO0.getGlassHeight() + "");
                    } else if (!CBDOrderTools.isNeedGlass(key)) {
                        cell.setCellValue("/");
                    }

                    if (CBDOrderTools.isNeedGlass(key) && ProductTypeEnums.Complete.code == materialVO.getProductType() && c == 2) {
                        cell.setCellValue(materialInfoVO0.getGlassWidth() + "");
                    } else if (!CBDOrderTools.isNeedGlass(key)) {
                        cell.setCellValue("/");
                    }

                    if (CBDOrderTools.isNeedGlass(key) && c == 3) {
                        cell.setCellValue(GlassColor.getNameByCode(materialInfoVO0.getGlassColor()));
                    } else if (!CBDOrderTools.isNeedGlass(key)) {
                        cell.setCellValue("/");
                    }

                    if (CBDOrderTools.isNeedGlass(key) && ProductTypeEnums.Complete.code == materialVO.getProductType() && c == 4) {
                        cell.setCellValue(materialInfoVO0.getMaterialNum());
                    } else if (!CBDOrderTools.isNeedGlass(key)) {
                        cell.setCellValue("/");
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
                        cell.setCellValue(materialInfoVO0.getHandlePlace() + "");
                    }

                    if (j == 5) {
                        cell.setCellValue(materialInfoVO0.getHingeLocation() + "");
                    }

                    if (j == 6) {
                        cell.setCellValue(materialInfoVO0.getDirection() + "");
                    }

                    if (j == 7) {
                        cell.setCellValue("");
                    }

                    if (j == 8) {
                        cell.setCellValue(materialInfoVO0.getRemark() + "");
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
            //放置横梁信息 TransomTypeEnums.getEnumByCode(materialVO.getTransoms().get(0).getTransomType())
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
                        cell.setCellValue(infoVO.getTransomNum() + "");
                    }
                    if (j == 7) {
                        cell.setCellValue(infoVO.getRemark() + "");
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
                    cell.setCellValue(infoVO.getIronwareName() + "");
                }
                if (e == 3) {
                    cell.setCellValue(infoVO.getIronwareNum() + "");
                }
                if (e == 4) {
                    cell.setCellValue(infoVO.getUnit() + "");
                }
                if (e == 5) {
                    cell.setCellValue(IronwareColorEnums.getEnumByCode(infoVO.getIronwareColor()));
                }
                if (e == 6) {
                    cell.setCellValue(infoVO.getSpecification() + "");
                }
                if (e == 7) {
                    cell.setCellValue(infoVO.getRemark() + "");
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

    public List<MaterialInfo> saveMaterialInfoList(MaterialInfoMapper mapper, String uid, List<MaterialInfoVO> materialInfoVOS) {

        List<MaterialInfo> result = new ArrayList<>();
        for (MaterialInfoVO materialInfoVO : materialInfoVOS) {

            MaterialInfo info = new MaterialInfo(uid, MaterialColorEnums.getByCode(materialInfoVO.getMaterialColor()),
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
            result.add(info);

        }
        return result;
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
        materialVO.setOrderType(orderInfo.getOrderType().code);
        materialVO.setOrderStatus(orderInfo.getOrderStatus().code);


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
            ironwareInfoVO.setIronwareNum(ironwareInfo.getIronwareNum());

            ironwareInfoVOS.add(ironwareInfoVO);

        }
        materialVO.setIronwares(ironwareInfoVOS);


        return materialVO;


    }

    public void saveCustomer(String adminUid, ProductVO productVO, OrderInfo orderInfo) {
        //保存客户信息
        CustomerVO customerVO = new CustomerVO();
        customerVO.setAdminUid(adminUid);
        customerVO.setCustomerResource(CustomerResourceEnums.ORDER.code);
        customerVO.setCustomerName(productVO.getCustomerName());
        customerVO.setCustomerAddr(productVO.getCustomerAddr());
        customerVO.setCutomerPhone(productVO.getCustomerPhoneNum());
        customerVO.setSalesman(productVO.getSalesman());
        customerVO.setCustomerNick(productVO.getCustomerNick());
        customerVO.setExpress(productVO.getExpress());
        customerVO.setNameMapper(productVO.getCustomerNick() + "-"+productVO.getCustomerName());

        customerVO.setOrderUid(orderInfo.getUid());
        customerVO.setOrderId(orderInfo.getOrderId());
        customerVO.setTotalPrice(orderInfo.getTotalPrice());
        //改成发送消息
        rabbitTemplate.convertAndSend(SysConf.EXCHANGE_DIRECT, SysConf.BINGO_WEB, JsonUtils.objectToJson(customerVO));
        //personFeignClient.saveCustomerByOrder(customerVO);
    }


    public String getRoleNameByAdminUid(String adminUid) throws Exception {

        String adminInfoJson = redisUtil.get(RedisConf.USER_ADMIN_INFO_KEY);

        if (null == adminInfoJson) {

            //更新用户权限相关

            List<Admin> admins = adminService.list();

            List<Map<String, String>> adminMapList = new ArrayList<>();

            for (Admin admin : admins) {
                String uid = admin.getUid();
                String username = admin.getUserName();
                String roleUid = admin.getRoleUid();
                Role role = roleService.getById(roleUid);

                Map<String, String> adminMap = new HashMap<>();
                adminMap.put("uid", uid);
                adminMap.put("username", username);
                adminMap.put("roleName", role.getRoleName());

                adminMapList.add(adminMap);
            }

            adminInfoJson = JsonUtils.objectToJson(adminMapList);
            redisUtil.setEx(RedisConf.USER_ADMIN_INFO_KEY, adminInfoJson, 2l, TimeUnit.HOURS);
        }

        List<Map<String, String>> adminInfoMapList = (List<Map<String, String>>) JsonUtils.jsonArrayToArrayList(adminInfoJson);

        List<Map<String, String>> adminMapList = adminInfoMapList.stream().filter(new Predicate<Map<String, String>>() {
            @Override
            public boolean test(Map<String, String> stringStringMap) {

                String uid = stringStringMap.get("uid");

                if (uid.equals(adminUid)) {
                    return true;
                } else {
                    return false;
                }

            }
        }).collect(Collectors.toList());

        return adminMapList.get(0).get("roleName");

    }
}
