package com.bingo.test;

import com.bingo.erp.xo.global.ExcelConf;
import com.bingo.erp.xo.tools.OrderTools;
import com.bingo.erp.xo.vo.IronwareInfoVO;
import com.bingo.erp.xo.vo.MaterialInfoVO;
import com.bingo.erp.xo.vo.MaterialVO;
import com.bingo.erp.xo.vo.TransomVO;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TDHLTest {


    @Test
    public void test1() {

        MaterialVO materialVO = new MaterialVO();
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

        List<IronwareInfoVO> ironwareInfoVOS = new ArrayList<>();

        ironwareInfoVOS.add(ironwareInfoVO);
        ironwareInfoVOS.add(ironwareInfoVO);
        ironwareInfoVOS.add(ironwareInfoVO);


        TransomVO transomVO = new TransomVO();
        transomVO.setTransomType(1);
        transomVO.setTransomColor(1);
        transomVO.setHeight(new BigDecimal("299"));
        transomVO.setTransomNum(4);
        transomVO.setPrice(new BigDecimal("278"));
        transomVO.setTotalPrice(transomVO.getPrice().multiply(new BigDecimal(transomVO.getTransomNum())).setScale(0, BigDecimal.ROUND_HALF_UP));

        List<TransomVO> transomVOS = new ArrayList<>();
        transomVOS.add(transomVO);
        transomVOS.add(transomVO);
        transomVOS.add(transomVO);

        materialVO.setMaterials(materialInfoVOS);
        materialVO.setTransoms(transomVOS);
        materialVO.setIronwares(ironwareInfoVOS);


        try {

            int materialNum = materialVO.getMaterials().size() - 1;

            int ironwareNum = materialVO.getIronwares().size() - 1;

            int transomNum = materialVO.getTransoms().size() - 1;

            OrderTools tools = new OrderTools();

            POIFSFileSystem fs = new POIFSFileSystem(new File(ExcelConf.SRC_FILE_URL + ExcelConf.TDHL_PRODUCT_ORDER_FILENAME));

            HSSFWorkbook wb = new HSSFWorkbook(fs);

            HSSFSheet sheet = wb.getSheetAt(0);


            Map<String, List<MaterialInfoVO>> map = tools.materialsToMap(materialVO.getMaterials());

            //int addnum = tools.productExtensionExcel(sheet, map, materialNum, ironwareNum, transomNum);

            //填充料玻、五金数据
            //tools.productFillData(sheet, addnum,map, materialVO,materialVO.getIronwares());

            File newFile1 = new File("/Users/drew/IdeaProject/bg-erp/excel/production-order-tdhl-n.xls");

            FileOutputStream outputStream = new FileOutputStream(newFile1);
            wb.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}





















