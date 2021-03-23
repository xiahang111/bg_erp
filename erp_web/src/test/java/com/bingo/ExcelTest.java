package com.bingo;

import com.bingo.erp.utils.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.List;

public class ExcelTest {

    public static void main(String[] args) {


    }

    @Test
    public void test1() {
        try {
            String file_src = "/Users/drew/Desktop/data_excel/";

            String[] month = new String[]{
                    "3","4","5","6","7","8","9","10","11","12"
            };
            String[] keyword = new String[]{
                    "安装费","安装工资"
            };
            String keyTitle = "";
            for (String key:keyword) {
                keyTitle += key + ",";
            }


            BigDecimal totalPrice = new BigDecimal("0");

            File file = new File(file_src + "2020.xlsx");

            //POIFSFileSystem poifsFileSystem = new POIFSFileSystem(file);

            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {

                BigDecimal sheetPrice = new BigDecimal("0");

                XSSFSheet sheet = workbook.getSheetAt(i);
                String monthName = sheet.getSheetName();

                if (isContain(monthName, month)) {

                    for (int j = 3; j < sheet.getLastRowNum(); j++) {
                        XSSFRow row = sheet.getRow(j);
                        if (row != null) {
                            if (keyword.length > 0) {
                                String title = row.getCell(1) != null ? row.getCell(1).getStringCellValue():"";
                                if (isContain(title, keyword)) {
                                    sheetPrice = sheetPrice.add(getPrice(row));
                                    totalPrice = totalPrice.add(getPrice(row));
                                }

                            } else {
                                sheetPrice = sheetPrice.add(getPrice(row));
                                totalPrice = totalPrice.add(getPrice(row));
                            }
                        }
                    }
                    System.out.println("当前月份：" + monthName + ",关键字：" + keyTitle + ",当前页总金额：" + sheetPrice);

                }

            }
            System.out.println("关键字：" + keyTitle + ",总金额：" + totalPrice);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        try {
            String file_src = "/Users/drew/Desktop/data_excel/";

            String[] month = new String[]{
                    "1"
            };
            String[] keyword = new String[]{
                    "安装费","安装工资"
            };
            String keyTitle = "";
            for (String key:keyword) {
                keyTitle += key + ",";
            }


            BigDecimal totalPrice = new BigDecimal("0");

            File file = new File(file_src + "2021.xls");

            //POIFSFileSystem poifsFileSystem = new POIFSFileSystem(file);

            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {

                BigDecimal sheetPrice = new BigDecimal("0");

                HSSFSheet sheet = workbook.getSheetAt(i);
                String monthName = sheet.getSheetName();

                if (isContain(monthName, month)) {

                    for (int j = 3; j < sheet.getLastRowNum(); j++) {
                        HSSFRow row = sheet.getRow(j);
                        if (row != null) {
                            if (keyword.length > 0) {
                                String title = row.getCell(1) != null ? row.getCell(1).getStringCellValue():"";
                                if (isContain(title, keyword)) {
                                    sheetPrice = sheetPrice.add(getPrice(row));
                                    totalPrice = totalPrice.add(getPrice(row));
                                }

                            } else {
                                sheetPrice = sheetPrice.add(getPrice(row));
                                totalPrice = totalPrice.add(getPrice(row));
                            }
                        }
                    }
                    System.out.println("当前月份：" + monthName + ",关键字：" + keyTitle + ",当前页总金额：" + sheetPrice);

                }

            }
            System.out.println("关键字：" + keyTitle + ",总金额：" + totalPrice);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private BigDecimal getPrice(Row row) {

        BigDecimal result = new BigDecimal(0);

        try {
            result = new BigDecimal(row.getCell(3).getNumericCellValue());
            return result;
        } catch (Exception e) {

            return result;
        }

    }


    private boolean isContain(String title, String[] keyword) {

        for (String key : keyword) {
            if (title.contains(key)) {
                return true;
            }
        }

        return false;

    }
}
