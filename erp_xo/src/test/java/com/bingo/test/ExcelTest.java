package com.bingo.test;

import com.bingo.erp.base.enums.*;
import com.bingo.erp.commons.entity.IronwareInfo;
import com.bingo.erp.xo.global.ExcelConf;
import com.bingo.erp.xo.global.NormalConf;
import com.bingo.erp.xo.vo.IronwareInfoVO;
import com.bingo.erp.xo.vo.MaterialInfoVO;
import com.bingo.erp.xo.vo.MaterialVO;
import net.sf.jxls.transformer.XLSTransformer;
import net.sf.jxls.util.Util;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class ExcelTest {

    @Test
    public void Test1() {

        MaterialVO vo = new MaterialVO();

        List<MaterialInfoVO> materialInfoVOS = new ArrayList<>();
        List<IronwareInfoVO> ironwareInfoVOS = new ArrayList<>();

        vo.setProductType(1);
        vo.setCustomerName("马化腾");
        vo.setCustomerNick("江西扛把子");
        vo.setCustomerPhoneNum("15201290813");
        vo.setCustomerAddr("江西省九江市湖口县海正阳光10-1-101");
        vo.setBigPackageNum(3);
        vo.setSimplePackageNum(0);
        vo.setOrderDate(new Date());
        vo.setDeliveryDate(new Date());
        vo.setOrderId("P-门2005027");
        vo.setSalesman("马云");
        vo.setOrderMaker("高圆圆");
        vo.setBigPackageNum(4);
        vo.setSimplePackageNum(6);


        MaterialInfoVO materialInfoVO = new MaterialInfoVO();
        materialInfoVO.setMaterialColor(1);
        materialInfoVO.setMaterialType(1);
        materialInfoVO.setHandleType(1);
        materialInfoVO.setGlassColor(1);
        materialInfoVO.setHeight(new BigDecimal("2448"));
        materialInfoVO.setWidth(new BigDecimal("415"));
        materialInfoVO.setHandlePlace("从下往上");
        materialInfoVO.setDirection("对开");
        materialInfoVO.setRemark("无");
        materialInfoVO.setPrice(new BigDecimal("345"));
        BigDecimal area = materialInfoVO.getHeight().
                multiply(materialInfoVO.getWidth()).
                divide(new BigDecimal("1000000")).setScale(2, RoundingMode.CEILING);
        BigDecimal totalPrice = materialInfoVO.getPrice().multiply(area).setScale(0,RoundingMode.HALF_UP);

        materialInfoVO.setArea(area);
        materialInfoVO.setTotalPrice(totalPrice);

        materialInfoVOS.add(materialInfoVO);
        materialInfoVOS.add(materialInfoVO);


        IronwareInfoVO ironwareInfoVO = new IronwareInfoVO();
        ironwareInfoVO.setIronwareName("20全盖合页");
        ironwareInfoVO.setUnit("个");
        ironwareInfoVO.setIronwareColor(1);
        ironwareInfoVO.setSpecification("20 * 4");
        ironwareInfoVO.setPrice(new BigDecimal("32"));
        ironwareInfoVO.setRemark("无");
        ironwareInfoVO.setIronwareNum(52);
        BigDecimal ironTotalPrice = ironwareInfoVO.getPrice().multiply(new BigDecimal(ironwareInfoVO.getIronwareNum())).setScale(0,RoundingMode.HALF_UP);

        ironwareInfoVO.setTotalPrice(ironTotalPrice);

        ironwareInfoVOS.add(ironwareInfoVO);
        ironwareInfoVOS.add(ironwareInfoVO);
        ironwareInfoVOS.add(ironwareInfoVO);

        vo.setMaterials(materialInfoVOS);
        vo.setIronwares(ironwareInfoVOS);

        System.out.println(vo.toString());

        Map dataMap = new HashMap();

        dataMap.put("orderId", vo.getOrderId());
        dataMap.put("customerNick", vo.getCustomerNick());
        dataMap.put("orderDate", vo.getOrderDate());
        dataMap.put("deliveryDate", vo.getDeliveryDate());
        dataMap.put("materialColor", MaterialColorEnums.HTLS.name);
        dataMap.put("materialType", MaterialTypeEnums.ZB20.name);
        dataMap.put("handleType", "1100拉手");
        dataMap.put("glassColor", GlassColor.CYLS.name);
        dataMap.put("customerName", vo.getCustomerName());
        dataMap.put("customerPhoneNum", vo.getCustomerPhoneNum());
        dataMap.put("customerAddr", vo.getCustomerAddr());
        dataMap.put("simplePackageNum", vo.getSimplePackageNum());
        dataMap.put("bigPackageNum", vo.getBigPackageNum());

        try {

            FileInputStream inputStream = new FileInputStream(new File("/Users/drew/IdeaProject/bg-erp/excel/door-order.xls"));

            POIFSFileSystem fs = new POIFSFileSystem(inputStream);

            HSSFWorkbook wb = new HSSFWorkbook(fs);

            HSSFSheet sheet = wb.getSheetAt(0);
            System.out.println(sheet.getLastRowNum());

            int materialNum = materialInfoVOS.size() - 1;
            int ironwareNum = ironwareInfoVOS.size() - 1;
            int MATERIAL_START_NUM = 10;
            int IRONWARE_START_NUM = 13 + materialNum;

            sheet.shiftRows(MATERIAL_START_NUM, sheet.getLastRowNum(), materialNum, true, false);
            sheet.shiftRows(IRONWARE_START_NUM, sheet.getLastRowNum(), ironwareNum, true, false);

            for (int i = 0; i < materialNum; i++) {
                Util.copyRow(sheet, sheet.getRow(MATERIAL_START_NUM - 1), sheet.getRow(MATERIAL_START_NUM + i));
            }

            for (int i = 0; i < ironwareNum; i++) {
                Util.copyRow(sheet, sheet.getRow(IRONWARE_START_NUM - 1), sheet.getRow(IRONWARE_START_NUM + i));
            }

            //拼接内容

            for (int i = 0; i < materialInfoVOS.size(); i++) {

                MaterialInfoVO materialInfoVO0 = materialInfoVOS.get(i);

                HSSFRow row = sheet.getRow(ExcelConf.MATERIAL_START_NUM - 1 + i);

                for (int j = 0; j < row.getLastCellNum(); j++) {
                    HSSFCell cell = row.getCell(j);

                    if (j == 0) {
                        cell.setCellValue("B");
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
                }

            }


            for (int l = 0; l < ironwareInfoVOS.size(); l++) {

                IronwareInfoVO ironwareInfoVO0 = ironwareInfoVOS.get(l);

                HSSFRow row = sheet.getRow(12 + materialInfoVOS.size() - 1 + l);

                for (int m = 0; m < row.getLastCellNum(); m++) {
                    HSSFCell cell = row.getCell(m);

                    if (m == 1) {
                        cell.setCellValue(ironwareInfoVO0.getIronwareName());
                    }

                    if (m == 3) {
                        cell.setCellValue("个");
                    }

                    if (m == 4) {
                        cell.setCellValue(ironwareInfoVO0.getIronwareColor());
                    }

                    if (m == 5) {
                        cell.setCellValue(ironwareInfoVO0.getSpecification());
                    }
                }

            }


            File newFile = new File("/Users/drew/IdeaProject/bg-erp/excel/door-order-new.xls");

            wb.write(newFile);

            XLSTransformer transformer = new XLSTransformer();
            transformer.transformXLS("/Users/drew/IdeaProject/bg-erp/excel/door-order-new.xls", dataMap, "/Users/drew/IdeaProject/bg-erp/excel/door-order-new.xls");


        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    @Test
    public void test2() {

        MaterialVO vo = new MaterialVO();

        List<MaterialInfoVO> materialInfoVOS = new ArrayList<>();
        List<IronwareInfoVO> ironwareInfoVOS = new ArrayList<>();

        vo.setProductType(1);
        vo.setCustomerName("马化腾");
        vo.setCustomerNick("江西扛把子");
        vo.setCustomerPhoneNum("15201290813");
        vo.setCustomerAddr("江西省九江市湖口县海正阳光10-1-101");
        vo.setBigPackageNum(3);
        vo.setSimplePackageNum(0);
        vo.setOrderDate(new Date());
        vo.setDeliveryDate(new Date());
        vo.setOrderId("P-门2005027");
        vo.setSalesman("马云");
        vo.setOrderMaker("高圆圆");
        vo.setBigPackageNum(4);
        vo.setSimplePackageNum(6);


        MaterialInfoVO materialInfoVO = new MaterialInfoVO();
        materialInfoVO.setMaterialColor(1);
        materialInfoVO.setMaterialType(1);
        materialInfoVO.setHandleType(1);
        materialInfoVO.setGlassColor(1);
        materialInfoVO.setHeight(new BigDecimal("2448"));
        materialInfoVO.setWidth(new BigDecimal("415"));
        materialInfoVO.setHandlePlace("从下往上");
        materialInfoVO.setDirection("对开");
        materialInfoVO.setRemark("无==备注");
        materialInfoVO.setPrice(new BigDecimal("345"));
        materialInfoVO.setMaterialHeight(new BigDecimal("2448"));
        materialInfoVO.setMaterialWidth(new BigDecimal("415"));
        materialInfoVO.setMaterialNum(3);
        materialInfoVO.setHandlePlace("上往下100");
        materialInfoVO.setHingeLocation("附图");
        materialInfoVO.setGlassHeight(new BigDecimal("466"));
        materialInfoVO.setGlassWidth(new BigDecimal("377"));
        materialInfoVO.setGlassColor(1);
        materialInfoVO.setMaterialDetail("466竖料2支；377横料2支");

        BigDecimal area = materialInfoVO.getHeight().
                multiply(materialInfoVO.getWidth()).
                divide(new BigDecimal("1000000")).setScale(2, RoundingMode.CEILING);
        BigDecimal totalPrice = materialInfoVO.getPrice().multiply(area).setScale(2);

        materialInfoVO.setArea(area);
        materialInfoVO.setTotalPrice(totalPrice);

        materialInfoVOS.add(materialInfoVO);
        materialInfoVOS.add(materialInfoVO);
        materialInfoVOS.add(materialInfoVO);
        materialInfoVOS.add(materialInfoVO);

        MaterialInfoVO materialInfoVO1 = new MaterialInfoVO();

        materialInfoVO1.setMaterialColor(1);
        materialInfoVO1.setMaterialType(1);
        materialInfoVO1.setHandleType(1);
        materialInfoVO1.setGlassColor(1);
        materialInfoVO1.setHeight(new BigDecimal("2448"));
        materialInfoVO1.setWidth(new BigDecimal("415"));
        materialInfoVO1.setHandlePlace("从下往上");
        materialInfoVO1.setDirection("对开");
        materialInfoVO1.setRemark("无==备注");
        materialInfoVO1.setPrice(new BigDecimal("345"));
        materialInfoVO1.setMaterialHeight(new BigDecimal("2448"));
        materialInfoVO1.setMaterialWidth(new BigDecimal("415"));
        materialInfoVO1.setMaterialNum(3);
        materialInfoVO1.setHandlePlace("上往下100");
        materialInfoVO1.setHingeLocation("附图");
        materialInfoVO1.setGlassHeight(new BigDecimal("466"));
        materialInfoVO1.setGlassWidth(new BigDecimal("377"));
        materialInfoVO1.setGlassColor(1);
        materialInfoVO1.setMaterialDetail("466竖料2支；377横料2支");

        BigDecimal area1 = materialInfoVO.getHeight().
                multiply(materialInfoVO.getWidth()).
                divide(new BigDecimal("1000000")).setScale(2, RoundingMode.CEILING);
        BigDecimal totalPrice1 = materialInfoVO.getPrice().multiply(area).setScale(2);

        materialInfoVO1.setArea(area1);
        materialInfoVO1.setTotalPrice(totalPrice1);

        materialInfoVOS.add(materialInfoVO1);
        materialInfoVOS.add(materialInfoVO1);
        materialInfoVOS.add(materialInfoVO1);

        IronwareInfoVO ironwareInfoVO = new IronwareInfoVO();
        ironwareInfoVO.setIronwareName("20全盖合页");
        ironwareInfoVO.setUnit("个");
        ironwareInfoVO.setIronwareColor(1);
        ironwareInfoVO.setSpecification("20 * 4");
        ironwareInfoVO.setPrice(new BigDecimal("32"));
        ironwareInfoVO.setRemark("无");
        ironwareInfoVO.setIronwareNum(52);
        BigDecimal ironTotalPrice = ironwareInfoVO.getPrice().multiply(new BigDecimal(ironwareInfoVO.getIronwareNum()));

        ironwareInfoVO.setTotalPrice(ironTotalPrice);

        ironwareInfoVOS.add(ironwareInfoVO);
        ironwareInfoVOS.add(ironwareInfoVO);
        ironwareInfoVOS.add(ironwareInfoVO);
        ironwareInfoVOS.add(ironwareInfoVO);
        ironwareInfoVOS.add(ironwareInfoVO);

        vo.setMaterials(materialInfoVOS);
        vo.setIronwares(ironwareInfoVOS);

        System.out.println(vo.toString());

        Map dataMap = new HashMap();

        dataMap.put("orderId", vo.getOrderId());
        dataMap.put("customerNick", vo.getCustomerNick());
        dataMap.put("orderDate", vo.getOrderDate());
        dataMap.put("deliveryDate", vo.getDeliveryDate());
        dataMap.put("materialColor", MaterialColorEnums.HTLS.name);
        dataMap.put("materialType", MaterialTypeEnums.ZB20.name);
        dataMap.put("handleType", "1100拉手");
        dataMap.put("glassColor", GlassColor.CYLS.name);
        dataMap.put("customerName", vo.getCustomerName());
        dataMap.put("customerPhoneNum", vo.getCustomerPhoneNum());
        dataMap.put("customerAddr", vo.getCustomerAddr());
        dataMap.put("simplePackageNum", vo.getSimplePackageNum());
        dataMap.put("bigPackageNum", vo.getBigPackageNum());

        try {

            POIFSFileSystem fs = new POIFSFileSystem(new File("/Users/drew/IdeaProject/bg-erp/excel/production-order.xls"));

            HSSFWorkbook wb = new HSSFWorkbook(fs);

            HSSFSheet sheet = wb.getSheetAt(0);
            System.out.println(sheet.getLastRowNum());

            int materialNum = materialInfoVOS.size() - 1;
            int ironwareNum = ironwareInfoVOS.size() - 1;
            int MATERIAL_START_NUM = 10;
            int GLASS_START_NUM = 13 + materialNum;

            int IRONWARE_START_NUM = 16 + materialNum * 2;

            sheet.shiftRows(MATERIAL_START_NUM, sheet.getLastRowNum(), materialNum, true, false);
            sheet.shiftRows(GLASS_START_NUM, sheet.getLastRowNum(), materialNum, true, false);
            sheet.shiftRows(IRONWARE_START_NUM, sheet.getLastRowNum(), ironwareNum, true, false);

            for (int i = 0; i < materialNum; i++) {
                Util.copyRow(sheet, sheet.getRow(MATERIAL_START_NUM - 1), sheet.getRow(MATERIAL_START_NUM + i));
                Util.copyRow(sheet, sheet.getRow(GLASS_START_NUM - 1), sheet.getRow(GLASS_START_NUM + i));
            }

            for (int i = 0; i < ironwareNum; i++) {
                Util.copyRow(sheet, sheet.getRow(IRONWARE_START_NUM - 1), sheet.getRow(IRONWARE_START_NUM + i));
            }

            Map<Integer, String> letMap = new HashMap<>();
            letMap.put(0, "A");
            letMap.put(1, "B");
            letMap.put(2, "C");
            letMap.put(3, "D");
            letMap.put(4, "E");

            for (int a = 0; a < vo.getMaterials().size(); a++) {

                MaterialInfoVO infoVO = vo.getMaterials().get(a);

                HSSFRow materialRow = sheet.getRow(MATERIAL_START_NUM - 1 + a);
                HSSFRow glassRow = sheet.getRow(GLASS_START_NUM - 1 + a);

                for (int b = 0; b < materialRow.getLastCellNum(); b++) {
                    HSSFCell cell = materialRow.getCell(b);

                    if (b == 0) {
                        cell.setCellValue(letMap.get(a));
                    }

                    if (b == 1) {
                        cell.setCellValue(infoVO.getMaterialHeight() + "");
                    }

                    if (b == 2) {
                        cell.setCellValue(infoVO.getMaterialWidth() + "");
                    }
                    if (b == 3) {
                        cell.setCellValue(infoVO.getMaterialNum());

                    }
                    if (b == 4) {
                        cell.setCellValue(infoVO.getHandlePlace());

                    }
                    if (b == 5) {

                        cell.setCellValue(infoVO.getHingeLocation());
                    }
                    if (b == 6) {
                        cell.setCellValue(infoVO.getDirection());
                    }
                    if (b == 7) {
                        cell.setCellValue("");
                    }
                    if (b == 8) {
                        cell.setCellValue(infoVO.getRemark());
                    }
                }

                for (int c = 0; c < glassRow.getLastCellNum(); c++) {
                    HSSFCell cell = glassRow.getCell(c);

                    if (c == 0) {
                        cell.setCellValue(letMap.get(a));
                    }
                    if (c == 1) {
                        cell.setCellValue(infoVO.getGlassHeight() + "");
                    }
                    if (c == 2) {
                        cell.setCellValue(infoVO.getGlassWidth() + "");
                    }
                    if (c == 3) {
                        cell.setCellValue(GlassColor.getNameByCode(infoVO.getGlassColor()));
                    }
                    if (c == 4) {
                        cell.setCellValue(infoVO.getMaterialNum());
                    }
                    if (c == 5) {
                        cell.setCellValue(infoVO.getMaterialDetail());
                    }
                }

            }


            for (int d = 0; d < vo.getIronwares().size(); d++) {

                IronwareInfoVO infoVO = vo.getIronwares().get(d);

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
                        cell.setCellValue(infoVO.getIronwareColor());
                    }
                    if (e == 6) {
                        cell.setCellValue(infoVO.getSpecification());
                    }
                    if (e == 7) {
                        cell.setCellValue(infoVO.getRemark());
                    }

                }

            }


            File newFile = new File("/Users/drew/IdeaProject/bg-erp/excel/production-order-new.xls");

            wb.write(newFile);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    @Test
    public void test3() {

        List<MaterialInfoVO> materialInfoVOS = new ArrayList<>();

        MaterialInfoVO materialInfoVO = new MaterialInfoVO();
        materialInfoVO.setMaterialColor(1);
        materialInfoVO.setMaterialType(1001);
        materialInfoVO.setHandleType(1);
        materialInfoVO.setGlassColor(1);
        materialInfoVO.setHeight(new BigDecimal("2448"));
        materialInfoVO.setWidth(new BigDecimal("415"));
        materialInfoVO.setHandlePlace("从下往上");
        materialInfoVO.setDirection("对开");
        materialInfoVO.setRemark("无==备注");
        materialInfoVO.setPrice(new BigDecimal("345"));
        materialInfoVO.setMaterialHeight(new BigDecimal("2448"));
        materialInfoVO.setMaterialWidth(new BigDecimal("415"));
        materialInfoVO.setMaterialNum(3);
        materialInfoVO.setHandlePlace("上往下100");
        materialInfoVO.setHingeLocation("附图");
        materialInfoVO.setGlassHeight(new BigDecimal("466"));
        materialInfoVO.setGlassWidth(new BigDecimal("377"));
        materialInfoVO.setGlassColor(1);
        materialInfoVO.setMaterialDetail("466竖料2支；377横料2支");

        BigDecimal area = materialInfoVO.getHeight().
                multiply(materialInfoVO.getWidth()).
                divide(new BigDecimal("1000000")).setScale(2, RoundingMode.CEILING);
        BigDecimal totalPrice = materialInfoVO.getPrice().multiply(area).setScale(2);

        materialInfoVO.setArea(area);
        materialInfoVO.setTotalPrice(totalPrice);

        materialInfoVOS.add(materialInfoVO);
        materialInfoVOS.add(materialInfoVO);
        materialInfoVOS.add(materialInfoVO);

        MaterialInfoVO materialInfoVO1 = new MaterialInfoVO();

        materialInfoVO1.setMaterialColor(2);
        materialInfoVO1.setMaterialType(1001);
        materialInfoVO1.setHandleType(1);
        materialInfoVO1.setGlassColor(1);
        materialInfoVO1.setHeight(new BigDecimal("2448"));
        materialInfoVO1.setWidth(new BigDecimal("415"));
        materialInfoVO1.setHandlePlace("从下往上");
        materialInfoVO1.setDirection("对开");
        materialInfoVO1.setRemark("无==备注");
        materialInfoVO1.setPrice(new BigDecimal("345"));
        materialInfoVO1.setMaterialHeight(new BigDecimal("2448"));
        materialInfoVO1.setMaterialWidth(new BigDecimal("415"));
        materialInfoVO1.setMaterialNum(3);
        materialInfoVO1.setHandlePlace("上往下100");
        materialInfoVO1.setHingeLocation("附图");
        materialInfoVO1.setGlassHeight(new BigDecimal("466"));
        materialInfoVO1.setGlassWidth(new BigDecimal("377"));
        materialInfoVO1.setGlassColor(1);
        materialInfoVO1.setMaterialDetail("466竖料2支；377横料2支");

        BigDecimal area1 = materialInfoVO.getHeight().
                multiply(materialInfoVO.getWidth()).
                divide(new BigDecimal("1000000")).setScale(2, RoundingMode.CEILING);
        BigDecimal totalPrice1 = materialInfoVO.getPrice().multiply(area).setScale(2);

        materialInfoVO1.setArea(area1);
        materialInfoVO1.setTotalPrice(totalPrice1);

        materialInfoVOS.add(materialInfoVO1);
        materialInfoVOS.add(materialInfoVO1);


        MaterialInfoVO materialInfoVO2 = new MaterialInfoVO();

        materialInfoVO2.setMaterialColor(2);
        materialInfoVO2.setMaterialType(1002);
        materialInfoVO2.setHandleType(1);
        materialInfoVO2.setGlassColor(1);
        materialInfoVO2.setHeight(new BigDecimal("2448"));
        materialInfoVO2.setWidth(new BigDecimal("415"));
        materialInfoVO2.setHandlePlace("从下往上");
        materialInfoVO2.setDirection("对开");
        materialInfoVO2.setRemark("无==备注");
        materialInfoVO2.setPrice(new BigDecimal("345"));
        materialInfoVO2.setMaterialHeight(new BigDecimal("2448"));
        materialInfoVO2.setMaterialWidth(new BigDecimal("415"));
        materialInfoVO2.setMaterialNum(3);
        materialInfoVO2.setHandlePlace("上往下100");
        materialInfoVO2.setHingeLocation("附图");
        materialInfoVO2.setGlassHeight(new BigDecimal("466"));
        materialInfoVO2.setGlassWidth(new BigDecimal("377"));
        materialInfoVO2.setGlassColor(1);
        materialInfoVO2.setMaterialDetail("466竖料2支；377横料2支");

        BigDecimal area2 = materialInfoVO.getHeight().
                multiply(materialInfoVO.getWidth()).
                divide(new BigDecimal("1000000")).setScale(2, RoundingMode.CEILING);
        BigDecimal totalPrice2 = materialInfoVO.getPrice().multiply(area).setScale(2);

        materialInfoVO2.setArea(area2);
        materialInfoVO2.setTotalPrice(totalPrice2);

        materialInfoVOS.add(materialInfoVO2);
        materialInfoVOS.add(materialInfoVO2);

        MaterialInfoVO materialInfoVO3 = new MaterialInfoVO();

        materialInfoVO3.setMaterialColor(2);
        materialInfoVO3.setMaterialType(1003);
        materialInfoVO3.setHandleType(1);
        materialInfoVO3.setGlassColor(1);
        materialInfoVO3.setHeight(new BigDecimal("2448"));
        materialInfoVO3.setWidth(new BigDecimal("415"));
        materialInfoVO3.setHandlePlace("从下往上");
        materialInfoVO3.setDirection("对开");
        materialInfoVO3.setRemark("无==备注");
        materialInfoVO3.setPrice(new BigDecimal("345"));
        materialInfoVO3.setMaterialHeight(new BigDecimal("2448"));
        materialInfoVO3.setMaterialWidth(new BigDecimal("415"));
        materialInfoVO3.setMaterialNum(3);
        materialInfoVO3.setHandlePlace("上往下100");
        materialInfoVO3.setHingeLocation("附图");
        materialInfoVO3.setGlassHeight(new BigDecimal("466"));
        materialInfoVO3.setGlassWidth(new BigDecimal("377"));
        materialInfoVO3.setGlassColor(1);
        materialInfoVO3.setMaterialDetail("466竖料2支；377横料2支");

        BigDecimal area3 = materialInfoVO.getHeight().
                multiply(materialInfoVO.getWidth()).
                divide(new BigDecimal("1000000")).setScale(2, RoundingMode.CEILING);
        BigDecimal totalPrice3 = materialInfoVO.getPrice().multiply(area).setScale(2);

        materialInfoVO3.setArea(area3);
        materialInfoVO3.setTotalPrice(totalPrice3);

        materialInfoVOS.add(materialInfoVO3);
        materialInfoVOS.add(materialInfoVO3);

        MaterialInfoVO materialInfoVO4 = new MaterialInfoVO();

        materialInfoVO4.setMaterialColor(2);
        materialInfoVO4.setMaterialType(2001);
        materialInfoVO4.setHandleType(1);
        materialInfoVO4.setGlassColor(1);
        materialInfoVO4.setHeight(new BigDecimal("2448"));
        materialInfoVO4.setWidth(new BigDecimal("415"));
        materialInfoVO4.setHandlePlace("从下往上");
        materialInfoVO4.setDirection("对开");
        materialInfoVO4.setRemark("无==备注");
        materialInfoVO4.setPrice(new BigDecimal("345"));
        materialInfoVO4.setMaterialHeight(new BigDecimal("2448"));
        materialInfoVO4.setMaterialWidth(new BigDecimal("415"));
        materialInfoVO4.setMaterialNum(3);
        materialInfoVO4.setHandlePlace("上往下100");
        materialInfoVO4.setHingeLocation("附图");
        materialInfoVO4.setGlassHeight(new BigDecimal("466"));
        materialInfoVO4.setGlassWidth(new BigDecimal("377"));
        materialInfoVO4.setGlassColor(1);
        materialInfoVO4.setMaterialDetail("466竖料2支；377横料2支");

        BigDecimal area4 = materialInfoVO.getHeight().
                multiply(materialInfoVO.getWidth()).
                divide(new BigDecimal("1000000")).setScale(2, RoundingMode.CEILING);
        BigDecimal totalPrice4 = materialInfoVO.getPrice().multiply(area).setScale(2);

        materialInfoVO4.setArea(area4);
        materialInfoVO4.setTotalPrice(totalPrice4);

        materialInfoVOS.add(materialInfoVO4);
        materialInfoVOS.add(materialInfoVO4);



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

        System.out.println("============");

        try {

            FileInputStream inputStream = new FileInputStream(new File("/Users/drew/IdeaProject/bg-erp/excel/production-order-n.xls"));

            POIFSFileSystem fs = new POIFSFileSystem(inputStream);

            HSSFWorkbook wb = new HSSFWorkbook(fs);

            HSSFSheet sheet = wb.getSheetAt(0);

            int addNum = (map.keySet().size() - 1) * 2;

            for (String key : map.keySet()) {

                addNum += map.get(key).size();

            }

            addNum = addNum - 1;

            sheet.shiftRows(10, sheet.getLastRowNum(), addNum);


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

                    File newFile = new File("/Users/drew/IdeaProject/bg-erp/excel/production-order-n"+ia+".xls");

                    System.out.println("ia==="+ia);
                    FileOutputStream outputStream = new FileOutputStream(newFile);
                    wb.write(outputStream);

                    point = point + add;
                    ia++;
                }


            }


            int rowNum_glass = 13 + addNum;

            sheet.shiftRows(rowNum_glass, sheet.getLastRowNum(), addNum, true, false);

            File newFile = new File("/Users/drew/IdeaProject/bg-erp/excel/production-order-n"+5+".xls");

            System.out.println("ia==="+5);
            FileOutputStream outputStream1 = new FileOutputStream(newFile);
            wb.write(outputStream1);

            int ia1 = 0;
            int point11 = 0;
            for (String key : map.keySet()) {

                if (ia1 == 0) {

                    int add = map.get(key).size() - 1;

                    for (int j = 0; j < add; j++) {
                        Util.copyRow(sheet, sheet.getRow(12 + addNum), sheet.getRow(rowNum_glass + j));
                    }

                    point11 = 13 + addNum + add;
                    ia1++;

                } else {

                    int add = map.get(key).size() + 2;

                    for (int i = 0; i < add; i++) {
                        abcCell(sheet.getRow(point11 + i));

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

                }

            }




           /* Map<Integer, String> letMap = new HashMap<>();
            letMap.put(0, "A");
            letMap.put(1, "B");
            letMap.put(2, "C");
            letMap.put(3, "D");
            letMap.put(4, "E");

            int point1 = 10;
            int point2 = 13 + addNum;
            for (String key : map.keySet()) {

                HSSFRow titlerow = sheet.getRow(point1 - 3);
                HSSFRow titlerow2 = sheet.getRow(point2 - 3);

                HSSFCell titleCell = titlerow.getCell(0);
                HSSFCell titleCell2 = titlerow2.getCell(0);

                titleCell.setCellValue(key);
                titleCell2.setCellValue("玻璃材料（"+ key + ")");

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
                point2 += 2;

            } */

            File newFile1 = new File("/Users/drew/IdeaProject/bg-erp/excel/production-order-n-n-n.xls");

            FileOutputStream outputStream = new FileOutputStream(newFile1);
            wb.write(outputStream);


        } catch (Exception e) {

            e.printStackTrace();

        }


    }


    private void abcCell(HSSFRow row){

        if (row.getPhysicalNumberOfCells() < 1){
            for (int i = 0; i < 10; i++) {

                HSSFCell cell = row.createCell(i);
                cell.setCellValue("");


            }
        }

    }


}
