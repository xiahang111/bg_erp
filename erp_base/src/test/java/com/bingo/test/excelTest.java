package com.bingo.test;


import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import net.sf.jxls.util.Util;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class excelTest {

    @Test
    public void Test1() {

        try {

            POIFSFileSystem fs = new POIFSFileSystem(new File("/Users/drew/IdeaProject/bg-erp/excel/door-order.xls"));

            HSSFWorkbook wb = new HSSFWorkbook(fs);

            HSSFSheet sheet = wb.getSheetAt(0);
            System.out.println(sheet.getLastRowNum());

            Map dataMap = new HashMap();
            dataMap.put("customerNick", "夏杭");
            dataMap.put("orderId", "L-门2004030");

            int materialNum = 2;
            int ironwareNum = 3;
            int MATERIAL_START_NUM = 10;
            int IRONWARE_START_NUM = 14 + materialNum-1;



            sheet.shiftRows(MATERIAL_START_NUM, sheet.getLastRowNum(), materialNum, true, false);
            sheet.shiftRows(IRONWARE_START_NUM, sheet.getLastRowNum(), ironwareNum, true, false);

            for (int i = 0; i < materialNum; i++){
                Util.copyRow(sheet, sheet.getRow(MATERIAL_START_NUM-1), sheet.getRow(MATERIAL_START_NUM+i));
            }

            for (int i = 0; i < ironwareNum; i++){
                Util.copyRow(sheet, sheet.getRow(IRONWARE_START_NUM-1), sheet.getRow(IRONWARE_START_NUM+i));
            }



            File newFile = new File("/Users/drew/IdeaProject/bg-erp/excel/door-order-new.xls");

            wb.write(newFile);

             XLSTransformer transformer = new XLSTransformer();
            transformer.transformXLS("/Users/drew/IdeaProject/bg-erp/excel/door-order-new.xls",dataMap,"/Users/drew/IdeaProject/bg-erp/excel/door-order-new.xls");


        } catch (Exception e) {

            e.printStackTrace();

        }


    }

}


 /*for (int j = 0; j < row.getLastCellNum(); j++) {

                    int lc = row.getLastCellNum();
                    if(row.getCell(j) != null){
                        switch (row.getCell(j).getCellTypeEnum()){

                            case STRING:
                                System.out.println("源数据第"+i+"行第"+j+"列"+row.getCell(j).getStringCellValue());
                                break;
                            case NUMERIC:
                                System.out.println("源数据第"+i+"行第"+j+"列"+row.getCell(j).getNumericCellValue());
                                break;
                            default:
                                System.out.println("源数据第"+i+"行第"+j+"列"+row.getCell(j).getCellTypeEnum());
                        }
                    }


                }

                for (int k = 0; k < newRow.getLastCellNum(); k++) {

                    if(newRow.getCell(k) != null){
                        switch (newRow.getCell(k).getCellTypeEnum()){

                            case STRING:
                                System.out.println("目标数据第"+i+"行第"+k+"列"+newRow.getCell(k).getStringCellValue());
                                break;
                            case NUMERIC:
                                System.out.println("目标数据第"+i+"行第"+k+"列"+newRow.getCell(k).getNumericCellValue());
                                break;
                            default:
                                System.out.println("目标数据第"+i+"行第"+k+"列"+newRow.getCell(k).getCellTypeEnum());
                        }
                    }


                }*/