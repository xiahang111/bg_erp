package com.bingo.erp.xo.order.tools;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelTools {

    public class XSSFDateUtil extends DateUtil {

    }
    public static void main(String[] args) {
        List<String> files = new ArrayList<>();
        files.add("D:/workspace/bg_erp/excel/生产单20201024019062.xls");
        files.add("D:/workspace/bg_erp/excel/订单20201024019062.xls");

        HSSFWorkbook newExcelCreat = new HSSFWorkbook();

        try {

            for(int i = 0;i<files.size();i++) {//遍历每个源excel文件，fileNameList为源文件的名称集合
                InputStream in = new FileInputStream(files.get(i));
                ZipSecureFile.setMinInflateRatio(-1.0d);
                HSSFWorkbook fromExcel = new HSSFWorkbook(in);
                HSSFSheet oldSheet = fromExcel.getSheetAt(0);//模板文件Sheet1
                HSSFSheet newSheet = newExcelCreat.createSheet("Sheet"+(i+1)+"");
                copySheet(newExcelCreat, oldSheet, newSheet);
            }
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        String allFileName="D:/workspace/bg_erp/excel/fgModelPrint.xlsx";
        try {
            FileOutputStream fileOut = new FileOutputStream(allFileName);
            newExcelCreat.write(fileOut);
            fileOut.flush();
            fileOut.close();
            System.out.println("复制成功");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void copyCellStyle(HSSFCellStyle fromStyle, HSSFCellStyle toStyle) {
        toStyle.cloneStyleFrom(fromStyle);//此一行代码搞定
    }
    public static void mergeSheetAllRegion(HSSFSheet fromSheet, HSSFSheet toSheet) {//合并单元格
        int num = fromSheet.getNumMergedRegions();
        CellRangeAddress cellR = null;
        for (int i = 0; i < num; i++) {
            cellR = fromSheet.getMergedRegion(i);
            toSheet.addMergedRegion(cellR);
        }
    }

    public static void copyCell(HSSFWorkbook wb,HSSFCell fromCell, HSSFCell toCell) {
        HSSFCellStyle newstyle=wb.createCellStyle();
        copyCellStyle(fromCell.getCellStyle(), newstyle);
        //toCell.setEncoding(fromCell.getEncoding());
        //样式
        toCell.setCellStyle(newstyle);
        if (fromCell.getCellComment() != null) {
            toCell.setCellComment(fromCell.getCellComment());
        }
        // 不同数据类型处理
        int fromCellType = fromCell.getCellType();
        toCell.setCellType(fromCellType);
        if (fromCellType == XSSFCell.CELL_TYPE_NUMERIC) {
            if (XSSFDateUtil.isCellDateFormatted(fromCell)) {
                toCell.setCellValue(fromCell.getDateCellValue());
            } else {
                toCell.setCellValue(fromCell.getNumericCellValue());
            }
        } else if (fromCellType == XSSFCell.CELL_TYPE_STRING) {
            toCell.setCellValue(fromCell.getRichStringCellValue());
        } else if (fromCellType == XSSFCell.CELL_TYPE_BLANK) {
            // nothing21
        } else if (fromCellType == XSSFCell.CELL_TYPE_BOOLEAN) {
            toCell.setCellValue(fromCell.getBooleanCellValue());
        } else if (fromCellType == XSSFCell.CELL_TYPE_ERROR) {
            toCell.setCellErrorValue(fromCell.getErrorCellValue());
        } else if (fromCellType == XSSFCell.CELL_TYPE_FORMULA) {
            toCell.setCellFormula(fromCell.getCellFormula());
        } else { // nothing29
        }

    }

    public static void copyRow(HSSFWorkbook wb,HSSFRow oldRow,HSSFRow toRow){
        toRow.setHeight(oldRow.getHeight());
        for (Iterator cellIt = oldRow.cellIterator(); cellIt.hasNext();) {
            HSSFCell tmpCell = (HSSFCell) cellIt.next();
            HSSFCell newCell = toRow.createCell(tmpCell.getColumnIndex());
            copyCell(wb,tmpCell, newCell);
        }
    }
    public static void copySheet(HSSFWorkbook wb,HSSFSheet fromSheet, HSSFSheet toSheet) {
        mergeSheetAllRegion(fromSheet, toSheet);
        //设置列宽
        for(int i=0;i<=fromSheet.getRow(fromSheet.getFirstRowNum()).getLastCellNum();i++){
            toSheet.setColumnWidth(i,fromSheet.getColumnWidth(i));
        }
        for (Iterator rowIt = fromSheet.rowIterator(); rowIt.hasNext();) {
            HSSFRow oldRow = (HSSFRow) rowIt.next();
            HSSFRow newRow = toSheet.createRow(oldRow.getRowNum());
            copyRow(wb,oldRow,newRow);
        }
    }
}
