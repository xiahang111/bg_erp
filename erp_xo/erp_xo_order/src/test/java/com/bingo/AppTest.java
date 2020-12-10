package com.bingo;

import com.bingo.erp.base.enums.*;
import com.bingo.erp.commons.entity.IronwareInfo;
import com.bingo.erp.commons.entity.MetalInfo;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.utils.DateUtils;
import com.bingo.erp.utils.JsonUtils;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.order.vo.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }


    @Test
    public void test1(){

        String date = DateUtils.formateDate(new Date(),DateUtils.DAYFORMAT_STRING);

        Integer year = Integer.valueOf(date.substring(0,4));
        Integer month = Integer.valueOf(date.substring(4,6));
        Integer day = Integer.valueOf(date.substring(6,8));

    }

    @Test
    public void test2(){

        String path="C:\\Users\\Administrator\\Desktop\\订单生产单446415\\";
        String fileName = "料单.xls";

        Map<Integer,Map<Integer,String >> dataMap = new HashMap<>();

        int a = 0;
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new File(path + fileName));

            HSSFWorkbook wb = new HSSFWorkbook(fs);

            for (int i = 0; i < wb.getNumberOfSheets(); i++) {

                Sheet sheet = wb.getSheetAt(i);

                //获取最大行数
                int rownum = sheet.getPhysicalNumberOfRows();
                //获取第一行
                Row row = sheet.getRow(0);
                //获取最大列数
                int colnum = row.getPhysicalNumberOfCells();

                for (int j = 0 ;j<rownum;j++){
                    row = sheet.getRow(j);
                    if(row !=null){
                        for (int k=0;k<20;k++){
                            String cellData = (String) getCellFormatValue(row.getCell(k));
                            if(StringUtils.isNotBlank(cellData)){
                                System.out.println("第"+(j+1)+"行第"+(k+1)+"列数据-单元格数据:"+cellData);
                               Map<Integer,String > cellMap;
                                if (null == dataMap.get(j+1)){
                                    cellMap = new HashMap<>();
                                    cellMap.put((k+1),cellData);
                                    dataMap.put((j+1),cellMap);
                                }else {
                                    cellMap = dataMap.get(j+1);
                                    cellMap.put((k+1),cellData);
                                }
                            }

                        }
                    }else{
                        break;
                    }

                }


            }

            System.out.println("这单是"+analyze1(dataMap)==null?"未识别":analyze1(dataMap).name);
            ProductVO productVO = analyzeOrderInfo(dataMap);
            System.out.println(JsonUtils.objectToJson(productVO));

        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public OrderTypeEnums analyze1(Map<Integer, Map<Integer, String>> dataMap){

        for (Integer rowNum : dataMap.keySet()){

            Map<Integer, String> rowMap = dataMap.get(rowNum);
            for (Integer cellNum : rowMap.keySet()) {

                String data = rowMap.get(cellNum);

                if (data.contains("图例")){
                   return OrderTypeEnums.METAL;
                }
            }
        }

        return null;

    }





    public ProductVO analyzeOrderInfo(Map<Integer,Map<Integer,String >> dataMap)throws Exception{

        ProductVO productVO = new ProductVO();

        for (Integer rowNum:dataMap.keySet()){

            Map<Integer,String > rowMap = dataMap.get(rowNum);
            for (Integer cellNum:rowMap.keySet()) {

                String data = rowMap.get(cellNum).replace(" ","");


                if (data.contains("电话：")) {
                    productVO.setCustomerPhoneNum(null == rowMap.get(cellNum + 1) ? "" : rowMap.get(cellNum + 1));
                }
                if (data.contains("编号NO")) {
                    productVO.setOrderId(null == rowMap.get(cellNum + 1) ? "" : rowMap.get(cellNum + 1));
                }
                if (data.contains("谨致TO")) {
                    productVO.setCustomerNick(null == rowMap.get(cellNum + 2) ? "" : rowMap.get(cellNum + 2));
                    if (StringUtils.isBlank(productVO.getCustomerNick())) {
                        data = replaceSome(data, "谨致TO", " ", ":", "：");
                        productVO.setCustomerNick(data);
                    }
                }
                if (data.contains("下单日期")) {
                    // todo productVO.setCustomerNick(null == rowMap.get(cellNum+1)?"": rowMap.get(cellNum+1));
                }
                if (data.contains("交货日期")) {
                    // todo productVO.setCustomerNick(null == rowMap.get(cellNum+1)?"": rowMap.get(cellNum+1));
                }
                if (data.contains("是净尺寸")) {
                    productVO.setIsClear(true);
                }
                if (data.contains("半成品")) {
                    productVO.setProductType(ProductTypeEnums.NotComplete.code);
                }
                if (data.contains(" 成品")) {
                    productVO.setProductType(ProductTypeEnums.Complete.code);
                }
                if (data.contains("小包装")) {
                    productVO.setSimplePackageNum(Integer.valueOf(null == rowMap.get(cellNum + 1) ? "0" : rowMap.get(cellNum + 1).replace(".0", "")));
                }
                if (data.contains("大包装")) {
                    productVO.setBigPackageNum(Integer.valueOf(null == rowMap.get(cellNum + 1) ? "0" : rowMap.get(cellNum + 1).replace(".0", "")));
                }
                if (data.contains("制单人")) {
                    productVO.setOrderMaker(null == rowMap.get(cellNum + 2) ? "" : rowMap.get(cellNum + 2));
                }
                if (data.contains("业务员")) {
                    productVO.setSalesman(null == rowMap.get(cellNum + 1) ? "" : rowMap.get(cellNum + 1));
                }
                if (data.contains("收货地址")) {
                    productVO.setCustomerAddr(replaceSome(data, "收货地址", ":", "：", " "));
                }
                if (data.contains("物流")) {
                    productVO.setExpress(replaceSome(data, "物流", ":", "：", " "));
                }
                if (data.contains("总计")) {
                    productVO.setOrderTotalPrice(null == rowMap.get(cellNum + 6)?new BigDecimal("0"):new BigDecimal(rowMap.get(cellNum + 6)));
                }
                if (data.contains("收货人")) {

                    String a = replaceSome(data, "收货人", ":", "：", " ");
                    String phoneNum = checkCellphone(a);
                    productVO.setCustomerPhoneNum(phoneNum);
                    productVO.setCustomerName(a.replace(phoneNum, ""));

                }
            }
        }

        System.out.println(JsonUtils.objectToJson(productVO));
        return productVO;
    }


    public static Object getCellFormatValue(Cell cell){
        Object cellValue = null;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellTypeEnum()){
                case NUMERIC:{
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case FORMULA:{
                    //判断cell是否为日期格式
                    if(DateUtil.isCellDateFormatted(cell)){
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    }else{
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case STRING:{
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }

    public void analyzeMaterialInfo(Map<Integer,Map<Integer,String >> dataMap){


        List<MaterialInfoVO> materialInfos = new ArrayList<>();
        for (Integer rowNum:dataMap.keySet()) {
            if (null != dataMap.get(rowNum).get(1) && dataMap.get(rowNum).get(1).replaceAll("\r|\n","").contains("拉手)")){
                String materialInfomatrion = dataMap.get(rowNum).get(1);

                int materialColor = MaterialColorEnums.getCodeByInformation(materialInfomatrion);
                int materialType = MaterialTypeEnums.getCodeByInformation(materialInfomatrion);
                int handletype = HandleEnums.getCodeByInformation(materialInfomatrion);
                int glassColor = GlassColor.getCodeByInformation(materialInfomatrion);

                loop:
                for (int i = rowNum + 2; i < dataMap.size(); i++) {

                    if(dataMap.get(i).size() < 2){
                        rowNum = i;
                        break loop;
                    }else {

                        MaterialInfoVO materialInfo = new MaterialInfoVO();
                        materialInfo.setMaterialColor(materialColor);
                        materialInfo.setMaterialType(materialType);
                        materialInfo.setHandleType(handletype);
                        materialInfo.setGlassColor(glassColor);
                        materialInfo.setHeight(null == dataMap.get(i).get(2)?new BigDecimal("0"):new BigDecimal(dataMap.get(i).get(2)));
                        materialInfo.setWidth(null == dataMap.get(i).get(3)?new BigDecimal("0"):new BigDecimal(dataMap.get(i).get(3)));
                        materialInfo.setMaterialNum(null == dataMap.get(i).get(4)?0:Integer.valueOf(dataMap.get(i).get(4)));
                        materialInfo.setHandlePlace(null == dataMap.get(i).get(5)?"":dataMap.get(i).get(5));
                        materialInfo.setHingeLocation(null == dataMap.get(i).get(6)?"":dataMap.get(i).get(6));
                        materialInfo.setDirection(null == dataMap.get(i).get(7)?"":dataMap.get(i).get(7));
                        materialInfo.setArea(null == dataMap.get(i).get(8)?new BigDecimal("0"):new BigDecimal(dataMap.get(i).get(8)));
                        materialInfo.setPrice(null == dataMap.get(i).get(9)?new BigDecimal("0"):new BigDecimal(dataMap.get(i).get(9).replace("￥","")));
                        materialInfo.setTotalPrice(null == dataMap.get(i).get(10)?new BigDecimal("0"):new BigDecimal(dataMap.get(i).get(10).replace("￥","")));
                        materialInfo.setRemark((null == dataMap.get(i).get(11)?"":dataMap.get(i).get(11)));
                        materialInfos.add(materialInfo);
                    }

                }
            }

        }

        System.out.println(materialInfos.size());

    }
    private String replaceSome(String data,String ... arg){

        for (String a:arg) {
            data = data.replace(a,"");
        }

        return data;
    }

    public void analyzeOrderInfo1(Map<Integer,Map<Integer,String >> dataMap,Integer productType){


        OrderInfo orderInfo = new OrderInfo();
        for (Integer rowNum : dataMap.keySet()) {

            Map<Integer, String> rowMap = dataMap.get(rowNum);
            for (Integer cellNum : rowMap.keySet()) {

                String data = rowMap.get(cellNum);

                if (data.contains("编号NO")) {
                    orderInfo.setOrderId(null == rowMap.get(cellNum + 1) ? "" : rowMap.get(cellNum + 1));
                }
                if (data.contains("谨致TO")) {
                    orderInfo.setCustomerNick(null == rowMap.get(cellNum + 2) ? "" : rowMap.get(cellNum + 2));
                    if(StringUtils.isBlank(orderInfo.getCustomerNick())){
                        data = replaceSome(data,"谨致TO"," ",":","：");
                        orderInfo.setCustomerNick(data);
                    }
                }
                if (data.contains("下单日期")) {
                    // todo productVO.setCustomerNick(null == rowMap.get(cellNum+1)?"": rowMap.get(cellNum+1));
                }
                if (data.contains("交货日期")) {
                    // todo productVO.setCustomerNick(null == rowMap.get(cellNum+1)?"": rowMap.get(cellNum+1));
                }


            }
        }

    }

    public void analyzeMetal(Map<Integer,Map<Integer,String >> dataMap,Integer productType){

        List<MetalInfo> metalInfos = new ArrayList<>();
        List<IronwareInfo> ironwareInfos = new ArrayList<>();

        for (Integer rowNum:dataMap.keySet()) {
            if (null != dataMap.get(rowNum).get(1) && dataMap.get(rowNum).get(1).replaceAll("\r|\n", "").contains("型号名称")) {
                //材料单
                loop:
                for (int i = rowNum + 1; i < dataMap.size(); i++) {

                    if(dataMap.get(i).size() < 6){
                        break loop;
                    }else {
                        MetalInfo metalInfo = new MetalInfo();
                        metalInfo.setMetalName(null == dataMap.get(i).get(1)?"":dataMap.get(i).get(1));
                        String materialColor = null == dataMap.get(i).get(2)?"":dataMap.get(i).get(2);
                        metalInfo.setMaterialColor(MaterialColorEnums.getByName(materialColor));
                        metalInfo.setSpecification(null == dataMap.get(i).get(4)?"":dataMap.get(i).get(4));
                        metalInfo.setMetalNum(null == dataMap.get(i).get(5)?0:Integer.valueOf(dataMap.get(i).get(5).replace(".0","")));
                        metalInfo.setPrice(null == dataMap.get(i).get(6)?new BigDecimal("0"):new BigDecimal(dataMap.get(i).get(6)));
                        metalInfo.setTotalPrice(null == dataMap.get(i).get(7)?new BigDecimal("0"):new BigDecimal(dataMap.get(i).get(7)));
                        metalInfo.setRemark(null == dataMap.get(i).get(8)?"":dataMap.get(i).get(8));
                        metalInfos.add(metalInfo);
                    }

                }

            }
            if (null != dataMap.get(rowNum).get(1) && dataMap.get(rowNum).get(1).replaceAll("\r|\n", "").contains("配件名称")) {
                //材料单
                loop1:
                for (int i = rowNum + 1; i < dataMap.size(); i++) {

                    if(dataMap.get(i).size() < 3){
                        break loop1;
                    }else {
                        IronwareInfo ironwareInfo = new IronwareInfo();
                        ironwareInfo.setIronwareName(null == dataMap.get(i).get(1)?"":dataMap.get(i).get(1));
                        ironwareInfo.setIronwareColor(null == dataMap.get(i).get(2)?"":dataMap.get(i).get(2));
                        ironwareInfo.setSpecification(null == dataMap.get(i).get(4)?"":dataMap.get(i).get(4));
                        ironwareInfo.setIronwareNum(null == dataMap.get(i).get(5)?0:Integer.valueOf(dataMap.get(i).get(5).replace(".0","")));
                        ironwareInfo.setPrice(null == dataMap.get(i).get(6)?new BigDecimal("0"):new BigDecimal(dataMap.get(i).get(6)));
                        ironwareInfo.setTotalPrice(null == dataMap.get(i).get(7)?new BigDecimal("0"):new BigDecimal(dataMap.get(i).get(7)));
                        ironwareInfo.setRemark(null == dataMap.get(i).get(8)?"":dataMap.get(i).get(8));
                        ironwareInfos.add(ironwareInfo);
                    }

                }
            }
        }

        System.out.println(metalInfos.size() + "=="+ironwareInfos.size());

    }

    public void analyzeTransomVO(Map<Integer,Map<Integer,String >> dataMap){

        List<TransomVO> transomVOS = new ArrayList<>();

        for (Integer rowNum:dataMap.keySet()) {
            if (null != dataMap.get(rowNum).get(1) && dataMap.get(rowNum).get(1).replaceAll("\r|\n", "").contains("天地横梁")) {

                String transomInfomatrion = dataMap.get(rowNum).get(1);
                int transomType = TransomTypeEnums.getCodeByInformation(transomInfomatrion);
                loop:
                for (int i = rowNum + 2; i < dataMap.size(); i++) {

                    if(dataMap.get(i).size() < 3){
                        rowNum = i;
                        break loop;
                    }else {
                        TransomVO transomVO = new TransomVO();
                        transomVO.setTransomType(transomType);
                        transomVO.setTransomColor(null == dataMap.get(i).get(2)?0:MaterialColorEnums.getByName(dataMap.get(i).get(2)).code);
                        transomVO.setHeight(null == dataMap.get(i).get(3)?new BigDecimal("0"): new BigDecimal(dataMap.get(i).get(2)));
                        transomVO.setTransomNum(null == dataMap.get(i).get(4)?0:Integer.valueOf(dataMap.get(i).get(4)));
                        transomVO.setPrice(null == dataMap.get(i).get(5)?new BigDecimal("0"): new BigDecimal(dataMap.get(i).get(5)));
                        transomVO.setTotalPrice(null == dataMap.get(i).get(6)?new BigDecimal("0"): new BigDecimal(dataMap.get(i).get(6)));
                        transomVO.setRemark(null == dataMap.get(i).get(7)?"":dataMap.get(i).get(7));
                        transomVOS.add(transomVO);
                    }
                }


            }
        }

        System.out.println(transomVOS.size());

    }


    /**
     * 获取层板信息
     * @param dataMap
     */
    public void analyzeLaminate(Map<Integer,Map<Integer,String >> dataMap){

        List<LaminateInfoVO> laminateVOS = new ArrayList<>();

        for (Integer rowNum:dataMap.keySet()) {
            String title = dataMap.get(rowNum).get(1);
            if (null != title){title.replaceAll("\r|\n","");}
            if (null !=title && (title.contains("层板") || title.contains("酒格"))){

                int materialType = 0;
                if(title.contains("酒格")){
                    materialType = MaterialTypeEnums.CBDJJ.code;
                }
                if(title.contains("二代玻璃")){
                    materialType = MaterialTypeEnums.CBD2.code;
                }
                if(title.contains("一代")){
                    materialType = MaterialTypeEnums.CBD1.code;
                }
                int materialColor = MaterialColorEnums.getCodeByInformation(title);
                int glassColor = GlassColor.getCodeByInformation(title);


                loop:
                for (int i = rowNum + 2; i < dataMap.size(); i++) {

                    if(dataMap.get(i).size() < 3){
                        rowNum = i;
                        break loop;
                    }else {
                       LaminateInfoVO laminateInfoVO = new LaminateInfoVO();
                       laminateInfoVO.setMaterialColor(materialColor);
                       laminateInfoVO.setGlassColor(glassColor);
                       laminateInfoVO.setMaterialType(materialType);
                       laminateInfoVO.setWidth(null == dataMap.get(i).get(2)?new BigDecimal("0"):new BigDecimal(dataMap.get(i).get(2)));
                       laminateInfoVO.setDepth(null == dataMap.get(i).get(3)?new BigDecimal("0"):new BigDecimal(dataMap.get(i).get(3)));
                       laminateInfoVO.setLaminateNum(null == dataMap.get(i).get(4)?0:Integer.valueOf(dataMap.get(i).get(4)));
                       laminateInfoVO.setLightPlace(null == dataMap.get(i).get(5)?"":dataMap.get(i).get(5));
                       laminateInfoVO.setLinePlace(null == dataMap.get(i).get(6)?"":dataMap.get(i).get(6));
                       laminateInfoVO.setLineColor(null == dataMap.get(i).get(7)?LineColor.None.code:LineColor.getCodeByName(dataMap.get(i).get(7)));
                       laminateInfoVO.setPerimeter(null == dataMap.get(i).get(8)?new BigDecimal("0"):new BigDecimal(dataMap.get(i).get(8)));
                       laminateInfoVO.setPrice(null == dataMap.get(i).get(9)?new BigDecimal("0"):new BigDecimal(dataMap.get(i).get(9).replace("￥","")));
                       laminateInfoVO.setTotalPrice(null == dataMap.get(i).get(10)?new BigDecimal("0"):new BigDecimal(dataMap.get(i).get(10).replace("￥","")));
                       laminateInfoVO.setRemark(null == dataMap.get(i).get(11)?"":dataMap.get(i).get(11));
                       laminateVOS.add(laminateInfoVO);
                    }
                }

            }
        }
        System.out.println(laminateVOS.size());

    }

    @Test
    public void test4(){

        String str = "湖南伊慕李总13007331500";
        checkCellphone(str);
    }

    private String checkCellphone(String str) {
        // 将给定的正则表达式编译到模式中
        Pattern pattern = Pattern.compile("((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}");
        // 创建匹配给定输入与此模式的匹配器。
        Matcher matcher = pattern.matcher(str);
        //查找字符串中是否有符合的子字符串
        while (matcher.find()) {
            //查找到符合的即输出
            System.out.println("查询到一个符合的手机号码：" + matcher.group());
            return matcher.group();
        }
        return "";
    }

    public void analyze(Map<Integer,Map<Integer,String >> dataMap){
        List<IronwareInfoVO> ironwareInfos = new ArrayList<>();
        //获取五金信息
        for (Integer rowNum:dataMap.keySet()) {
            if (null != dataMap.get(rowNum).get(1) && dataMap.get(rowNum).get(1).replaceAll("\r|\n","").contains("五金配件")){

                int ironNum = 0;
                loop:
                for (int i =rowNum+1;i<dataMap.size();i++){
                    if(dataMap.get(i).get(2).replaceAll("\r|\n","").contains("包装费")){
                        break loop;
                    }else {
                        IronwareInfoVO newIron = new IronwareInfoVO();
                        newIron.setIronwareName(null == dataMap.get(i).get(2)?"":dataMap.get(i).get(2));
                        newIron.setUnit(null == dataMap.get(i).get(5)?"":dataMap.get(i).get(5));
                        String ironwareColor = null == dataMap.get(i).get(6)?"":dataMap.get(i).get(6);
                        newIron.setIronwareColor(IronwareColorEnums.getEnumByName(ironwareColor).code);
                        newIron.setSpecification(null == dataMap.get(i).get(7)?"":dataMap.get(i).get(7));
                        newIron.setIronwareNum(null == dataMap.get(i).get(8)?0:Integer.valueOf(dataMap.get(i).get(8)));
                        newIron.setPrice(null == dataMap.get(i).get(9)?new BigDecimal("0"):new BigDecimal(dataMap.get(i).get(9).replace("￥","")));
                        newIron.setTotalPrice(null == dataMap.get(i).get(10)?new BigDecimal("0"):new BigDecimal(dataMap.get(i).get(10).replace("￥","")));
                        newIron.setRemark(null == dataMap.get(i).get(11)?"":dataMap.get(i).get(11));

                        ironwareInfos.add(newIron);

                    }
                }

            }

        }
        System.out.println(ironwareInfos.size());

    }

    @Test
    public void test3(){


    }



}
