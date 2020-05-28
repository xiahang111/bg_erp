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
        materialInfoVO.setDirection(1);
        materialInfoVO.setRemark("无");
        materialInfoVO.setPrice(new BigDecimal("345"));
        BigDecimal area = materialInfoVO.getHeight().
                multiply(materialInfoVO.getWidth()).
                divide(new BigDecimal("1000000")).setScale(2, RoundingMode.CEILING);
        BigDecimal totalPrice = materialInfoVO.getPrice().multiply(area).setScale(2);

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
        BigDecimal ironTotalPrice = ironwareInfoVO.getPrice().multiply(new BigDecimal(ironwareInfoVO.getIronwareNum()));

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

            POIFSFileSystem fs = new POIFSFileSystem(new File("/Users/drew/IdeaProject/bg-erp/excel/door-order.xls"));

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
        materialInfoVO.setDirection(1);
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
                HSSFRow glassRow = sheet.getRow(GLASS_START_NUM -1 + a );

                for (int b = 0; b < materialRow.getLastCellNum(); b++) {
                    HSSFCell cell = materialRow.getCell(b);

                    if (b == 0) {
                        cell.setCellValue(letMap.get(a));
                    }

                    if (b == 1) {
                        cell.setCellValue(infoVO.getMaterialHeight() + "");
                    }

                    if(b == 2){
                        cell.setCellValue(infoVO.getMaterialWidth()+"");
                    }
                    if(b == 3){
                        cell.setCellValue(infoVO.getMaterialNum());

                    }
                    if(b == 4){
                        cell.setCellValue(infoVO.getHandlePlace());

                    }
                    if(b == 5){

                        cell.setCellValue(infoVO.getHingeLocation());
                    }
                    if(b == 6){
                        cell.setCellValue(DirectionsEnums.getEnumByCode(infoVO.getDirection()));
                    }
                    if(b == 7){
                        cell.setCellValue("");
                    }
                    if(b == 8){
                        cell.setCellValue(infoVO.getRemark());
                    }
                }

                for (int c = 0; c < glassRow.getLastCellNum(); c++) {
                    HSSFCell cell = glassRow.getCell(c);

                    if(c == 0){
                        cell.setCellValue(letMap.get(a));
                    }
                    if(c == 1){
                        cell.setCellValue(infoVO.getGlassHeight()+"");
                    }
                    if(c == 2){
                        cell.setCellValue(infoVO.getGlassWidth()+"");
                    }
                    if(c == 3){
                        cell.setCellValue(GlassColor.getNameByCode(infoVO.getGlassColor()));
                    }
                    if(c == 4){
                        cell.setCellValue(infoVO.getMaterialNum());
                    }
                    if(c == 5){
                        cell.setCellValue(infoVO.getMaterialDetail());
                    }
                }

            }


            for (int d = 0; d < vo.getIronwares().size(); d++) {

                IronwareInfoVO infoVO = vo.getIronwares().get(d);

                HSSFRow row = sheet.getRow(IRONWARE_START_NUM - 1 + d);

                for (int e = 0; e < row.getLastCellNum(); e++) {

                    HSSFCell cell = row.getCell(e);
                    if(e == 1){
                        cell.setCellValue(infoVO.getIronwareName());
                    }
                    if(e == 3){
                        cell.setCellValue(infoVO.getIronwareNum());
                    }
                    if(e == 4){
                        cell.setCellValue(infoVO.getUnit());
                    }
                    if(e == 5){
                        cell.setCellValue(infoVO.getIronwareColor());
                    }
                    if(e == 6){
                        cell.setCellValue(infoVO.getSpecification());
                    }
                    if(e == 7){
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


}
