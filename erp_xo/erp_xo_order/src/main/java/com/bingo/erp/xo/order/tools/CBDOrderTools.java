package com.bingo.erp.xo.order.tools;

import com.bingo.erp.base.enums.*;
import com.bingo.erp.base.fatocry.material.MaterialCalculateFactory;
import com.bingo.erp.commons.entity.LaminateInfo;
import com.bingo.erp.xo.order.enums.MaterialFactoryEnum;
import com.bingo.erp.xo.order.global.ExcelConf;
import com.bingo.erp.xo.order.global.NormalConf;
import com.bingo.erp.xo.order.mapper.LaminaterInfoMapper;
import com.bingo.erp.xo.order.vo.IronwareInfoVO;
import com.bingo.erp.xo.order.vo.LaminateInfoVO;
import com.bingo.erp.xo.order.vo.LaminateVO;
import com.bingo.erp.xo.order.vo.MaterialCalculateResultVO;
import net.sf.jxls.util.Util;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CBDOrderTools {


    //判断产品类型是否是8003
    public static boolean is8003(int materialType) {

        if (materialType == MaterialTypeEnums.CBDJJ.code) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean isNeedGlass(String name) {

        for (String key : ExcelConf.NO_GLASS_STRINGS) {

            if (name.contains(key)) {
                return false;
            }

        }

        return true;

    }

    public Map<String, List<LaminateInfoVO>> laminateToMap(List<LaminateInfoVO> laminateInfoVOS) {

        Map<String, List<LaminateInfoVO>> map = new HashMap<>();

        for (LaminateInfoVO infoVO : laminateInfoVOS) {

            String key = MaterialTypeEnums.getEnumByCode(infoVO.getMaterialType()) + "、" +
                    MaterialColorEnums.getEnumByCode(infoVO.getMaterialColor()) + "、" +
                    GlassColor.getNameByCode(infoVO.getGlassColor()) + "玻璃";

            List<LaminateInfoVO> infoVOS = map.get(key);

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

    public int extensionExcel(HSSFSheet sheet, LaminateVO laminateVO) {

        Map<String, List<LaminateInfoVO>> map = laminateToMap(laminateVO.getLaminateInfos());

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


        int ironwareNum = laminateVO.getIronwares().size() - 1;

        if (ironwareNum > 0) {
            int IRONWARE_START_NUM = ExcelConf.IRONWARE_START_NUM + addNum;

            sheet.shiftRows(IRONWARE_START_NUM, sheet.getLastRowNum(), ironwareNum, true, false);

            for (int i = 0; i < ironwareNum; i++) {
                Util.copyRow(sheet, sheet.getRow(IRONWARE_START_NUM - 1), sheet.getRow(IRONWARE_START_NUM + i));
            }
        }

        return addNum;

    }

    public void fillData(HSSFSheet sheet, LaminateVO laminateVO, int addnum) {

        Map<String, List<LaminateInfoVO>> map = laminateToMap(laminateVO.getLaminateInfos());

        List<IronwareInfoVO> ironwareInfoVOS = laminateVO.getIronwares();

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

            List<LaminateInfoVO> infoVOS = map.get(key);

            for (int i = 0; i < infoVOS.size(); i++) {

                LaminateInfoVO laminateInfoVO = infoVOS.get(i);

                HSSFRow row = sheet.getRow(point1 - 1);

                for (int j = 0; j < row.getLastCellNum(); j++) {
                    HSSFCell cell = row.getCell(j);

                    if (j == 0) {
                        cell.setCellValue(letMap.get(i));
                    }

                    if (j == 1) {
                        cell.setCellValue(laminateInfoVO.getWidth() + "");
                    }

                    if (j == 2) {
                        cell.setCellValue(laminateInfoVO.getDepth() + "");
                    }

                    if (j == 3) {
                        cell.setCellValue(laminateInfoVO.getLaminateNum() + "");
                    }

                    if (j == 4) {
                        cell.setCellValue(laminateInfoVO.getLightPlace() + "");
                    }

                    if (j == 5) {
                        cell.setCellValue(laminateInfoVO.getLinePlace() + "");
                    }

                    if (j == 6) {
                        cell.setCellValue(LineColor.getNameByCode(laminateInfoVO.getLineColor()));
                    }

                    if (j == 7) {
                        cell.setCellValue(laminateInfoVO.getPerimeter() + "");
                    }

                    if (j == 8) {
                        cell.setCellValue("￥" + laminateInfoVO.getPrice() + "");
                    }

                    if (j == 9) {
                        cell.setCellValue("￥" + laminateInfoVO.getTotalPrice() + "");
                    }

                    if (j == 10) {
                        cell.setCellValue(laminateInfoVO.getRemark() + "");
                    }
                }

                point1++;
            }

            point1 += 2;

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
                    cell.setCellValue(ironwareInfoVO0.getUnit());
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

    public Map<String, Object> toMap(LaminateVO laminateVO) {

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("orderId", laminateVO.getOrderId());
        dataMap.put("productType", ProductTypeEnums.getEnumByCode(laminateVO.getProductType()));
        dataMap.put("customerNick", laminateVO.getCustomerNick());
        dataMap.put("orderDate", laminateVO.getOrderDate());
        dataMap.put("deliveryDate", laminateVO.getDeliveryDate());
        dataMap.put("orderMaker", laminateVO.getOrderMaker());
        dataMap.put("salesman", laminateVO.getSalesman());
        dataMap.put("orderTotalPriceChina", OrderTools.convert(laminateVO.getOrderTotalPrice().intValue()));
        if (laminateVO.getIsClear()) {
            dataMap.put("isClear", "是");
        } else {
            dataMap.put("isClear", "不是");
        }

        dataMap.put("bigPackageNum", laminateVO.getBigPackageNum());
        dataMap.put("bigPackagePrice", NormalConf.BIG_PACKAGE_PRICE.multiply(new BigDecimal(laminateVO.getBigPackageNum()).setScale(2)));
        dataMap.put("simplePackageNum", laminateVO.getSimplePackageNum());
        dataMap.put("simplePackagePrice", NormalConf.SIMPLE_PACKAGE_PRICE.multiply(new BigDecimal(laminateVO.getSimplePackageNum()).setScale(2)));
        dataMap.put("ironTotalPrice", laminateVO.getIronTotalPrice());
        dataMap.put("laminateTotalPrice", "￥" + laminateVO.getLaminateTotalPrice());
        dataMap.put("orderRemark", laminateVO.getRemark());
        dataMap.put("customerName", laminateVO.getCustomerName());
        dataMap.put("customerPhoneNum", laminateVO.getCustomerPhoneNum());
        dataMap.put("customerAddr", laminateVO.getCustomerAddr());
        dataMap.put("orderTotalPrice", "￥" + laminateVO.getOrderTotalPrice());
        dataMap.put("express", laminateVO.getExpress());
        if (laminateVO.getBigPackageNum() > 0 || laminateVO.getSimplePackageNum() > 0) {

            int totalPackageNum = laminateVO.getBigPackageNum() + laminateVO.getSimplePackageNum();
            dataMap.put("packageType", "加强包装" + totalPackageNum + "个");
        } else {
            dataMap.put("packageType", "普通包装");
        }

        return dataMap;
    }

    public int productExtensionExcel(HSSFSheet sheet, LaminateVO laminateVO) {

        Map<String, List<LaminateInfoVO>> map = laminateToMap(laminateVO.getLaminateInfos());

        int ironwareNum = laminateVO.getIronwares().size() - 1;

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


    public void validateCell(HSSFRow row) {

        if (row.getPhysicalNumberOfCells() < 1) {
            for (int i = 0; i < 10; i++) {

                HSSFCell cell = row.createCell(i);
                cell.setCellValue("");


            }
        }

    }


    public void productFillData(HSSFSheet sheet, int addNum, LaminateVO laminateVO) {

        Map<String, List<LaminateInfoVO>> map = laminateToMap(laminateVO.getLaminateInfos());
        List<IronwareInfoVO> ironwareInfoVOS = laminateVO.getIronwares();

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

            List<LaminateInfoVO> infoVOS = map.get(key);

            for (int i = 0; i < infoVOS.size(); i++) {

                LaminateInfoVO laminateInfoVO = infoVOS.get(i);

                HSSFRow row = sheet.getRow(point1 - 1);

                HSSFRow row2 = sheet.getRow(point2 - 1);

                for (int c = 0; c < row2.getLastCellNum(); c++) {
                    HSSFCell cell = row2.getCell(c);

                    switch (c) {
                        case 0:
                            cell.setCellValue(letMap.get(i));
                            break;

                        case 1:
                            if (isNeedGlass(key) && !is8003(laminateInfoVO.getMaterialType()) &&ProductTypeEnums.Complete.code == laminateVO.getProductType()) {
                                cell.setCellValue(laminateInfoVO.getGlassWidth() + "");
                            } else if (!isNeedGlass(key)) {
                                cell.setCellValue("/");
                            }
                            break;

                        case 2:
                            if (isNeedGlass(key) && !is8003(laminateInfoVO.getMaterialType()) &&ProductTypeEnums.Complete.code == laminateVO.getProductType()) {
                                cell.setCellValue(laminateInfoVO.getGlassDepth() + "");
                            } else if (!isNeedGlass(key)) {
                                cell.setCellValue("/");
                            }
                            break;

                        case 3:
                            if (!is8003(laminateInfoVO.getMaterialType()) &&isNeedGlass(key)) {
                                cell.setCellValue(GlassColor.getNameByCode(laminateInfoVO.getGlassColor()));
                            } else if (!isNeedGlass(key)) {
                                cell.setCellValue("/");
                            }
                            break;

                        case 4:
                            if (isNeedGlass(key) && !is8003(laminateInfoVO.getMaterialType()) &&ProductTypeEnums.Complete.code == laminateVO.getProductType() && c == 4) {
                                cell.setCellValue(laminateInfoVO.getLaminateNum());
                            } else if (!isNeedGlass(key)) {
                                cell.setCellValue("/");
                            }
                            break;

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
                        cell.setCellValue(laminateInfoVO.getWidth() + "");
                    }

                    if (j == 2) {
                        cell.setCellValue(laminateInfoVO.getDepth() + "");
                    }

                    if (j == 3) {
                        cell.setCellValue(laminateInfoVO.getLaminateNum() + "");
                    }

                    if (j == 4) {
                        cell.setCellValue(laminateInfoVO.getLightPlace() + "");
                    }

                    if (j == 5) {
                        cell.setCellValue(laminateInfoVO.getLinePlace() + "");
                    }

                    if (j == 6) {
                        cell.setCellValue(LineColor.getNameByCode(laminateInfoVO.getLineColor()));
                    }

                    if (j == 7) {
                        cell.setCellValue(LightColor.getNameByCode(laminateInfoVO.getLightColor()));

                    }

                    if (j == 8) {
                        cell.setCellValue(laminateInfoVO.getPipeType() + "");
                    }

                    if (j == 9) {
                        cell.setCellValue(laminateInfoVO.getRemark() + "");
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


    public void orderCalculate(LaminateVO laminateVO) {

        BigDecimal laminateTotalPrice = new BigDecimal("0");

        for (LaminateInfoVO laminateInfoVO : laminateVO.getLaminateInfos()) {
            laminateTotalPrice = laminateTotalPrice.add(laminateInfoVO.getTotalPrice());
        }


        BigDecimal ironwareTotalPrice = new BigDecimal("0");


        for (IronwareInfoVO ironwareInfoVO : laminateVO.getIronwares()) {

            ironwareTotalPrice = ironwareTotalPrice.add(ironwareInfoVO.getTotalPrice());

        }

        BigDecimal packageTotalPrice = NormalConf.BIG_PACKAGE_PRICE.
                multiply(new BigDecimal(laminateVO.getBigPackageNum())).
                add(NormalConf.SIMPLE_PACKAGE_PRICE.multiply(new BigDecimal(laminateVO.getSimplePackageNum()))).setScale(0, BigDecimal.ROUND_HALF_UP);


        laminateVO.setIronTotalPrice(ironwareTotalPrice.add(packageTotalPrice).setScale(0, BigDecimal.ROUND_HALF_UP));

        laminateVO.setLaminateTotalPrice(laminateTotalPrice.setScale(0, BigDecimal.ROUND_HALF_UP));

        laminateVO.setOrderTotalPrice(laminateTotalPrice.add(laminateVO.getIronTotalPrice()).setScale(0, BigDecimal.ROUND_HALF_UP));

    }

    public List<LaminateInfo> saveLaminateInfoList(LaminaterInfoMapper mapper, String uid, List<LaminateInfoVO> laminateInfoVOS) {

        List<LaminateInfo> result = new ArrayList<>();
        for (LaminateInfoVO laminateInfoVO : laminateInfoVOS) {

            LaminateInfo info = new LaminateInfo(uid,
                    MaterialColorEnums.getByCode(laminateInfoVO.getGlassColor()),
                    MaterialTypeEnums.getByCode(laminateInfoVO.getMaterialType()),
                    GlassColor.getByCode(laminateInfoVO.getGlassColor()),
                    laminateInfoVO.getWidth(),
                    laminateInfoVO.getGlassWidth(),
                    laminateInfoVO.getDepth(),
                    laminateInfoVO.getGlassDepth(),
                    laminateInfoVO.getLaminateNum(),
                    laminateInfoVO.getLightPlace(),
                    laminateInfoVO.getLinePlace(),
                    LightColor.getByCode(laminateInfoVO.getLightColor()),
                    LineColor.getByCode(laminateInfoVO.getLineColor()),
                    laminateInfoVO.getPerimeter(),
                    laminateInfoVO.getPrice(),
                    laminateInfoVO.getTotalPrice(),
                    laminateInfoVO.getRemark());
            mapper.insert(info);
            result.add(info);

        }

        return result;
    }

    public void laminateCalculate(List<LaminateInfoVO> laminateInfos) throws Exception {

        for (LaminateInfoVO laminateInfoVO : laminateInfos) {

            //获取材料种类
            int materialType = laminateInfoVO.getMaterialType();

            MaterialCalculateFactory factory = (MaterialCalculateFactory) MaterialFactoryEnum.getFactoryClass(Integer.valueOf(materialType)).newInstance();
            MaterialCalculateResultVO vo = (MaterialCalculateResultVO) factory.calculate(laminateInfoVO);

            laminateInfoVO.setGlassWidth(vo.getGlassHeight());
            laminateInfoVO.setGlassDepth(vo.getGlassWidth());

            BigDecimal minxPeri = new BigDecimal("1.5").multiply(new BigDecimal(laminateInfoVO.getLaminateNum()));

            BigDecimal perimeter = laminateInfoVO.getDepth().add(laminateInfoVO.getWidth()).
                    multiply(new BigDecimal(2)).
                    multiply(new BigDecimal(laminateInfoVO.getLaminateNum())).divide(new BigDecimal("1000").setScale(5, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP);


            if (perimeter.compareTo(minxPeri) <= 0) {
                laminateInfoVO.setPerimeter(minxPeri);
                laminateInfoVO.setTotalPrice(laminateInfoVO.getPrice().multiply(minxPeri).setScale(0, BigDecimal.ROUND_HALF_UP));
            } else {
                laminateInfoVO.setPerimeter(perimeter);
                laminateInfoVO.setTotalPrice(laminateInfoVO.getPrice().multiply(perimeter).setScale(0, BigDecimal.ROUND_HALF_UP));
            }
        }


    }
}
