package com.bingo.erp.xo.order.tools;

import com.bingo.erp.base.enums.*;
import com.bingo.erp.commons.entity.*;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.order.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class AnalyzeTools {


    /**
     * excel数据提取五金数据
     */
    public List<IronwareInfoVO> analyzeIronInfos(Map<Integer, Map<Integer, String>> dataMap) {

        List<IronwareInfoVO> ironwareInfos = new ArrayList<>();
        //获取五金信息
        for (Integer rowNum : dataMap.keySet()) {
            if (null != dataMap.get(rowNum).get(1) && dataMap.get(rowNum).get(1).replaceAll("\r|\n", "").contains("五金配件")) {

                int ironNum = 0;
                loop:
                for (int i = rowNum + 1; i < dataMap.size(); i++) {
                    if (null != dataMap.get(i).get(2) && dataMap.get(i).get(2).replaceAll("\r|\n", "").contains("包装费")) {
                        break loop;
                    } else {
                        IronwareInfoVO newIron = new IronwareInfoVO();
                        newIron.setIronwareName(null == dataMap.get(i).get(2) ? "" : dataMap.get(i).get(2));
                        newIron.setUnit(null == dataMap.get(i).get(5) ? "" : dataMap.get(i).get(5));
                        String ironwareColor = null == dataMap.get(i).get(6) ? "" : dataMap.get(i).get(6);
                        newIron.setIronwareColor(IronwareColorEnums.getEnumByName(ironwareColor).code);
                        newIron.setSpecification(null == dataMap.get(i).get(7) ? "" : dataMap.get(i).get(7));
                        newIron.setIronwareNum(null == dataMap.get(i).get(8) ? 0 : Integer.valueOf(dataMap.get(i).get(8).replace(".0", "")));
                        newIron.setPrice(null == dataMap.get(i).get(9) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(9).replace("￥", "")).setScale(0, BigDecimal.ROUND_HALF_DOWN));
                        newIron.setTotalPrice(null == dataMap.get(i).get(10) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(10).replace("￥", "")).setScale(0, BigDecimal.ROUND_HALF_DOWN));
                        newIron.setRemark(null == dataMap.get(i).get(11) ? "" : dataMap.get(i).get(11));

                        if (StringUtils.isNotBlank(newIron.getIronwareName())) {
                            ironwareInfos.add(newIron);
                        }
                    }
                }

            }

        }
        return ironwareInfos;
    }

    /**
     * excel提取门单数据
     */
    public List<MaterialInfoVO> analyzeMaterialInfo(Map<Integer, Map<Integer, String>> dataMap) {


        List<MaterialInfoVO> materialInfos = new ArrayList<>();
        for (Integer rowNum : dataMap.keySet()) {
            if (null != dataMap.get(rowNum).get(1) && dataMap.get(rowNum).size() < 2 && dataMap.get(rowNum).get(1).replaceAll("\r|\n", "").contains("拉手")) {
                String materialInfomatrion = dataMap.get(rowNum).get(1);
                int materialColor = MaterialColorEnums.getCodeByInformation(materialInfomatrion);
                int materialType = MaterialTypeEnums.getCodeByInformation(materialInfomatrion);
                int handletype = HandleEnums.getCodeByInformation(materialInfomatrion);
                int glassColor = GlassColor.getCodeByInformation(materialInfomatrion);
                int isHaveBar = 1;
                if (materialInfomatrion.contains("不含静音条")) {
                    isHaveBar = 0;
                }
                loop:
                for (int i = rowNum + 2; i < dataMap.size(); i++) {

                    if (dataMap.get(i).size() < 6) {
                        rowNum = i;
                        break loop;
                    } else {
                        MaterialInfoVO materialInfo = new MaterialInfoVO();
                        materialInfo.setMaterialColor(materialColor);
                        materialInfo.setMaterialType(materialType);
                        materialInfo.setHandleType(handletype);
                        materialInfo.setGlassColor(glassColor);
                        materialInfo.setIsHaveBar(isHaveBar);
                        materialInfo.setHeight(null == dataMap.get(i).get(2) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(2).replace(".0", "")));
                        materialInfo.setWidth(null == dataMap.get(i).get(3) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(3).replace(".0", "")));
                        materialInfo.setMaterialNum(null == dataMap.get(i).get(4) ? 0 : Integer.valueOf(dataMap.get(i).get(4).replace(".0", "")));
                        materialInfo.setHandlePlace(null == dataMap.get(i).get(5) ? "" : dataMap.get(i).get(5));
                        materialInfo.setHingeLocation(null == dataMap.get(i).get(6) ? "" : dataMap.get(i).get(6));
                        materialInfo.setDirection(null == dataMap.get(i).get(7) ? "" : dataMap.get(i).get(7));
                        materialInfo.setArea(null == dataMap.get(i).get(8) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(8)));
                        materialInfo.setPrice(null == dataMap.get(i).get(9) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(9).replace("￥", "")).setScale(0, BigDecimal.ROUND_HALF_DOWN));
                        materialInfo.setTotalPrice(null == dataMap.get(i).get(10) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(10).replace("￥", "")).setScale(0, BigDecimal.ROUND_HALF_DOWN));
                        materialInfo.setRemark((null == dataMap.get(i).get(11) ? "" : dataMap.get(i).get(11)));
                        materialInfos.add(materialInfo);
                    }

                }
            }
        }
        return materialInfos;

    }

    /**
     * excel提取横梁数据
     *
     * @param dataMap
     */
    public List<TransomVO> analyzeTransomVO(Map<Integer, Map<Integer, String>> dataMap) {

        List<TransomVO> transomVOS = new ArrayList<>();

        for (Integer rowNum : dataMap.keySet()) {
            if (null != dataMap.get(rowNum).get(1) && dataMap.get(rowNum).get(1).replaceAll("\r|\n", "").contains("天地横梁")) {

                String transomInfomatrion = dataMap.get(rowNum).get(1);
                int transomType = TransomTypeEnums.getCodeByInformation(transomInfomatrion);
                loop:
                for (int i = rowNum + 2; i < dataMap.size(); i++) {

                    if (dataMap.get(i).size() < 3) {
                        rowNum = i;
                        break loop;
                    } else {
                        TransomVO transomVO = new TransomVO();
                        transomVO.setTransomType(transomType);
                        transomVO.setTransomColor(null == dataMap.get(i).get(2) ? 0 : MaterialColorEnums.getByName(dataMap.get(i).get(2)).code);
                        transomVO.setHeight(null == dataMap.get(i).get(6) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(6)).setScale(0, BigDecimal.ROUND_HALF_DOWN));
                        transomVO.setTransomNum(null == dataMap.get(i).get(8) ? 0 : Integer.valueOf(dataMap.get(i).get(8).replace(".0", "")));
                        transomVO.setPrice(null == dataMap.get(i).get(9) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(9)).setScale(0, BigDecimal.ROUND_HALF_DOWN));
                        transomVO.setTotalPrice(null == dataMap.get(i).get(10) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(10)).setScale(0, BigDecimal.ROUND_HALF_DOWN));
                        transomVO.setRemark(null == dataMap.get(i).get(11) ? "" : dataMap.get(i).get(11));
                        transomVOS.add(transomVO);
                    }
                }

            }
        }

        return transomVOS;

    }

    public ProductVO analyzeOrderInfo(Map<Integer, Map<Integer, String>> dataMap, Integer orderType) throws Exception {

        ProductVO productVO = new ProductVO();

        boolean isGetProductType = false;

        for (Integer rowNum : dataMap.keySet()) {

            Map<Integer, String> rowMap = dataMap.get(rowNum);
            for (Integer cellNum : rowMap.keySet()) {

                String data = rowMap.get(cellNum).replace(" ", "");


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

                if (data.contains("半成品") && !isGetProductType) {
                    productVO.setProductType(ProductTypeEnums.NotComplete.code);
                    isGetProductType = true;
                }
                if (data.contains("成品") && !data.contains("半") && !isGetProductType) {
                    productVO.setProductType(ProductTypeEnums.Complete.code);
                    isGetProductType = true;
                }

                if (data.contains("小包装")) {
                    productVO.setSimplePackageNum(Integer.valueOf(null == rowMap.get(cellNum + 1) ? "0" : rowMap.get(cellNum + 1).replace(".0", "")));
                }
                if (data.contains("大包装")) {
                    productVO.setBigPackageNum(Integer.valueOf(null == rowMap.get(cellNum + 1) ? "0" : rowMap.get(cellNum + 1).replace(".0", "")));
                }
                if (data.contains("制单人")) {
                    if (orderType == OrderTypeEnums.DOORORDER.code) {
                        productVO.setOrderMaker(null == rowMap.get(cellNum + 2) ? "" : rowMap.get(cellNum + 2));
                    } else if (orderType == OrderTypeEnums.DESK.code || orderType == OrderTypeEnums.HANGING.code || orderType == OrderTypeEnums.SPECIMEN.code) {
                        productVO.setOrderMaker(null == rowMap.get(cellNum) ? "" : replaceSome(rowMap.get(cellNum), "制单人", ":", "：", " "));
                    } else {
                        productVO.setOrderMaker(null == rowMap.get(cellNum + 1) ? "" : rowMap.get(cellNum + 1));
                    }
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
                if (data.contains("备注") && data.length() > 2) {
                    productVO.setRemark(replaceSome(data, "备注", ":", "：", " "));
                }
                if (orderType != OrderTypeEnums.DESK.code && (data.contains("总计") || data.contains("实收合计") )) {
                    productVO.setOrderTotalPrice(null == rowMap.get(cellNum + 6) ? new BigDecimal("0") : new BigDecimal(rowMap.get(cellNum + 6)));
                }
                if(orderType == OrderTypeEnums.HANGING.code && data.contains("货款合计")){
                    productVO.setOrderTotalPrice(null == rowMap.get(cellNum + 9) ? new BigDecimal("0") : new BigDecimal(rowMap.get(cellNum + 9)));
                }
                if (orderType == OrderTypeEnums.DESK.code && data.contains("货款总合计")) {
                    productVO.setOrderTotalPrice(null == rowMap.get(cellNum + 10) ? new BigDecimal("0") : new BigDecimal(rowMap.get(cellNum + 10)));
                }
                if (data.contains("收货人")) {
                    String a = replaceSome(data, "收货人", ":", "：", " ");
                    String phoneNum = checkCellphone(a);
                    productVO.setCustomerPhoneNum(phoneNum);
                    productVO.setCustomerName(a.replace(phoneNum, ""));
                }
            }
        }

        return productVO;
    }


    public OrderInfo castOrderInfo(ProductVO productVO, String adminUid) {

        OrderInfo orderInfo = new OrderInfo();

        orderInfo.setAdminUid(adminUid);
        orderInfo.setOrderStatus(OrderStatusEnums.STAY_CONFIRM);
        orderInfo.setCustomerNick(productVO.getCustomerNick());
        orderInfo.setCustomerName(productVO.getCustomerName());
        orderInfo.setCustomerAddr(productVO.getCustomerAddr());
        orderInfo.setCustomerPhoneNum(productVO.getCustomerPhoneNum());

        orderInfo.setBigPackageNum(productVO.getBigPackageNum());
        orderInfo.setSimplePackageNum(productVO.getSimplePackageNum());
        orderInfo.setOrderDate(productVO.getOrderDate());
        orderInfo.setDeliveryDate(productVO.getDeliveryDate());

        orderInfo.setOrderId(productVO.getOrderId());
        orderInfo.setSalesman(productVO.getSalesman());
        orderInfo.setOrderMaker(productVO.getOrderMaker());
        orderInfo.setTotalPrice(productVO.getOrderTotalPrice());
        orderInfo.setExpress(productVO.getExpress());

        return orderInfo;


    }

    public Integer analyzeOrderType(Map<Integer, Map<Integer, String>> dataMap) {

        for (Integer rowNum : dataMap.keySet()) {

            Map<Integer, String> rowMap = dataMap.get(rowNum);
            for (Integer cellNum : rowMap.keySet()) {

                String data = rowMap.get(cellNum);

                if (data.contains("图例")) {
                    return OrderTypeEnums.METAL.code;
                }

                if (data.contains("桌子")) {
                    return OrderTypeEnums.DESK.code;
                }

                if (data.contains("小样品")) {
                    return OrderTypeEnums.SPECIMEN.code;
                }

                if (data.contains("置物架")||data.contains("壁挂")) {
                    return OrderTypeEnums.HANGING.code;
                }
            }
        }

        return null;

    }

    private String replaceSome(String data, String... arg) {

        for (String a : arg) {
            data = data.replace(a, "");
        }

        return data;
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

    /**
     * 获取层板信息
     *
     * @param dataMap
     */
    public List<LaminateInfoVO> analyzeLaminate(Map<Integer, Map<Integer, String>> dataMap) {

        List<LaminateInfoVO> laminateVOS = new ArrayList<>();

        for (Integer rowNum : dataMap.keySet()) {
            String title = dataMap.get(rowNum).get(1);
            if (null != title) {
                title.replaceAll("\r|\n", "");
            }
            if (null != title && (title.contains("层板") || title.contains("酒格"))) {

                int materialType = 0;
                String pipe = null;
                if (title.contains("酒格")) {
                    materialType = MaterialTypeEnums.CBDJJ.code;
                    if (title.contains("双")) {
                        pipe = "双管";
                    }
                    if (title.contains("单")) {
                        pipe = "单管";
                    }
                }
                if (title.contains("二代玻璃")) {
                    materialType = MaterialTypeEnums.CBD2.code;
                }
                if (title.contains("一代")) {
                    materialType = MaterialTypeEnums.CBD1.code;
                }
                int materialColor = MaterialColorEnums.getCodeByInformation(title);
                int glassColor = GlassColor.getCodeByInformation(title);


                loop:
                for (int i = rowNum + 2; i < dataMap.size(); i++) {

                    if (dataMap.get(i).size() < 3) {
                        rowNum = i;
                        break loop;
                    } else {
                        LaminateInfoVO laminateInfoVO = new LaminateInfoVO();
                        laminateInfoVO.setMaterialColor(materialColor);
                        laminateInfoVO.setGlassColor(glassColor);
                        laminateInfoVO.setMaterialType(materialType);
                        if (null != pipe) {
                            laminateInfoVO.setPipeType(pipe);
                        }

                        laminateInfoVO.setWidth(null == dataMap.get(i).get(2) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(2)));
                        laminateInfoVO.setDepth(null == dataMap.get(i).get(3) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(3)));
                        laminateInfoVO.setLaminateNum(null == dataMap.get(i).get(4) ? 0 : Integer.valueOf(dataMap.get(i).get(4).replace(".0", "")));
                        laminateInfoVO.setLightPlace(null == dataMap.get(i).get(5) ? "" : dataMap.get(i).get(5));
                        laminateInfoVO.setLinePlace(null == dataMap.get(i).get(6) ? "" : dataMap.get(i).get(6));
                        laminateInfoVO.setLineColor(null == dataMap.get(i).get(7) ? LineColor.None.code : LineColor.getCodeByName(dataMap.get(i).get(7)));
                        laminateInfoVO.setPerimeter(null == dataMap.get(i).get(8) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(8)));
                        laminateInfoVO.setPrice(null == dataMap.get(i).get(9) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(9).replace("￥", "")).setScale(0, BigDecimal.ROUND_HALF_DOWN));
                        laminateInfoVO.setTotalPrice(null == dataMap.get(i).get(10) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(10).replace("￥", "")).setScale(0, BigDecimal.ROUND_HALF_DOWN));
                        laminateInfoVO.setRemark(null == dataMap.get(i).get(11) ? "" : dataMap.get(i).get(11));
                        laminateVOS.add(laminateInfoVO);
                    }
                }

            }
        }
        return laminateVOS;
    }

    public Map<Integer, Map<Integer, String>> getDataMap(String path, String fileName) {

        Map<Integer, Map<Integer, String>> dataMap = new HashMap<>();
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

                for (int j = 0; j < rownum; j++) {
                    row = sheet.getRow(j);
                    if (row != null) {
                        for (int k = 0; k < 20; k++) {
                            String cellData1 = (String) getCellFormatValue(row.getCell(k));
                            if (StringUtils.isNotBlank(cellData1)) {
                                String cellData = cellData1.replace(" ", "");
                                System.out.println("第" + (j + 1) + "行第" + (k + 1) + "列数据-单元格数据:" + cellData);
                                Map<Integer, String> cellMap;
                                if (null == dataMap.get(j + 1)) {
                                    cellMap = new HashMap<>();
                                    cellMap.put((k + 1), cellData);
                                    dataMap.put((j + 1), cellMap);
                                } else {
                                    cellMap = dataMap.get(j + 1);
                                    cellMap.put((k + 1), cellData);
                                }
                            }

                        }
                    } else {
                        break;
                    }

                }

            }

            return dataMap;

        } catch (Exception e) {
            e.printStackTrace();
            log.info("读取报价单失败,原因:" + e.getMessage());
            return null;
        }

    }

    public static Object getCellFormatValue(Cell cell) {
        Object cellValue = null;
        if (cell != null) {
            //判断cell类型
            switch (cell.getCellTypeEnum()) {
                case NUMERIC: {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case FORMULA: {
                    //判断cell是否为日期格式
                    if (DateUtil.isCellDateFormatted(cell)) {
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    } else {
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case STRING: {
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }


    public ProductVO castProduct(ProductVO productVO, Integer type) {

        if (type == 1) {
            MaterialVO materialVO = new MaterialVO();
            materialVO.setIsClear(productVO.getIsClear());
            materialVO.setProductType(productVO.getProductType());
            materialVO.setOrderType(productVO.getOrderType());
            materialVO.setOrderStatus(productVO.getOrderStatus());
            materialVO.setCustomerName(productVO.getCustomerName());
            materialVO.setCustomerNick(productVO.getCustomerNick());
            materialVO.setCustomerAddr(productVO.getCustomerAddr());
            materialVO.setCustomerPhoneNum(productVO.getCustomerPhoneNum());
            materialVO.setExpress(productVO.getExpress());
            materialVO.setSalesman(productVO.getSalesman());
            materialVO.setOrderMaker(productVO.getOrderMaker());
            materialVO.setOrderTotalPrice(productVO.getOrderTotalPrice());
            materialVO.setRemark(productVO.getRemark());
            materialVO.setOrderId(productVO.getOrderId());
            materialVO.setBigPackageNum(productVO.getBigPackageNum());
            materialVO.setSimplePackageNum(productVO.getSimplePackageNum());
            materialVO.setOrderDate(productVO.getOrderDate());
            materialVO.setDeliveryDate(productVO.getDeliveryDate());
            return materialVO;
        } else {
            LaminateVO materialVO = new LaminateVO();
            materialVO.setIsClear(productVO.getIsClear());
            materialVO.setProductType(productVO.getProductType());
            materialVO.setOrderType(productVO.getOrderType());
            materialVO.setOrderStatus(productVO.getOrderStatus());
            materialVO.setCustomerName(productVO.getCustomerName());
            materialVO.setCustomerNick(productVO.getCustomerNick());
            materialVO.setCustomerAddr(productVO.getCustomerAddr());
            materialVO.setCustomerPhoneNum(productVO.getCustomerPhoneNum());
            materialVO.setExpress(productVO.getExpress());
            materialVO.setSalesman(productVO.getSalesman());
            materialVO.setOrderMaker(productVO.getOrderMaker());
            materialVO.setOrderTotalPrice(productVO.getOrderTotalPrice());
            materialVO.setRemark(productVO.getRemark());
            materialVO.setOrderId(productVO.getOrderId());
            materialVO.setBigPackageNum(productVO.getBigPackageNum());
            materialVO.setSimplePackageNum(productVO.getSimplePackageNum());
            materialVO.setOrderDate(productVO.getOrderDate());
            materialVO.setDeliveryDate(productVO.getDeliveryDate());
            return materialVO;
        }

    }


    public List<HangingInfo> analyzeHanging(Map<Integer, Map<Integer, String>> dataMap, String orderUid) {

        List<HangingInfo> hangingInfos = new ArrayList<>();

        for (Integer rowNum : dataMap.keySet()) {
            if (null != dataMap.get(rowNum).get(2) && dataMap.get(rowNum).get(2).replaceAll("\r|\n", "").contains("名称")) {


                //置物架
                loop:
                for (int i = rowNum + 1; i < dataMap.size(); i++) {

                    if (dataMap.get(i).size() < 3) {
                        break loop;
                    } else {

                        HangingInfo hangingInfo = new HangingInfo();
                        hangingInfo.setOrderInfouid(orderUid);
                        hangingInfo.setHangingName(null == dataMap.get(i).get(2) ? "" : dataMap.get(i).get(2));
                        hangingInfo.setSpecification(null == dataMap.get(i).get(4) ? "" : dataMap.get(i).get(4));
                        String color = null == dataMap.get(i).get(6) ? "" : dataMap.get(i).get(6).replace(" ","");
                        hangingInfo.setMaterialColor(MaterialColorEnums.getByName(color));
                        hangingInfo.setHangingNum(null == dataMap.get(i).get(7) ? 0 : Integer.valueOf(dataMap.get(i).get(7).replace(".0", "")));
                        hangingInfo.setPrice(null == dataMap.get(i).get(8) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(8)).setScale(0, BigDecimal.ROUND_HALF_DOWN));
                        hangingInfo.setTotalPrice(null == dataMap.get(i).get(9) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(9)).setScale(0, BigDecimal.ROUND_HALF_DOWN));
                        hangingInfo.setRemark(null == dataMap.get(i).get(10) ? "" : dataMap.get(i).get(10));

                        hangingInfos.add(hangingInfo);
                    }
                }
            }
        }

        return hangingInfos;
    }

    /**
     * 解析小样品
     *
     * @param dataMap
     * @param orderUid
     * @return
     */
    public List<SpecimenInfo> analyzeSpecimen(Map<Integer, Map<Integer, String>> dataMap, String orderUid) {

        List<SpecimenInfo> specimenInfos = new ArrayList<>();

        for (Integer rowNum : dataMap.keySet()) {
            if (null != dataMap.get(rowNum).get(1) && dataMap.get(rowNum).get(1).replaceAll("\r|\n", "").contains("名称")) {

                //小样品
                loop:
                for (int i = rowNum + 1; i < dataMap.size(); i++) {

                    if (dataMap.get(i).size() < 3) {
                        break loop;
                    } else {
                        SpecimenInfo specimenInfo = new SpecimenInfo();
                        specimenInfo.setOrderInfouid(orderUid);
                        specimenInfo.setSpecimenName((null == dataMap.get(i).get(1) ? "" : dataMap.get(i).get(1)));
                        specimenInfo.setSpecification(null == dataMap.get(i).get(3) ? "" : dataMap.get(i).get(3));
                        specimenInfo.setSpecimenNum(null == dataMap.get(i).get(5) ? 0 : Integer.valueOf(dataMap.get(i).get(5).replace(".0", "")));
                        specimenInfo.setPrice(null == dataMap.get(i).get(6) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(6)).setScale(0, BigDecimal.ROUND_HALF_DOWN));
                        specimenInfo.setTotalPrice(null == dataMap.get(i).get(7) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(7)).setScale(0, BigDecimal.ROUND_HALF_DOWN));
                        specimenInfo.setRemark(null == dataMap.get(i).get(8) ? "" : dataMap.get(i).get(8));

                        specimenInfos.add(specimenInfo);

                    }
                }

            }
        }

        return specimenInfos;
    }

    /**
     * 处理桌子单
     *
     * @param dataMap
     * @param orderUid
     * @return
     */
    public Map<String, Object> analyzeDesk(Map<Integer, Map<Integer, String>> dataMap, String orderUid) {

        Map<String, Object> result = new HashMap<>();
        List<DeskInfo> deskInfos = new ArrayList<>();
        List<IronwareInfo> ironwareInfos = new ArrayList<>();

        for (Integer rowNum : dataMap.keySet()) {
            if (null != dataMap.get(rowNum).get(1) && dataMap.get(rowNum).get(1).replaceAll("\r|\n", "").contains("桌子")) {

                //桌子腿
                loop:
                for (int i = rowNum + 2; i < dataMap.size(); i++) {

                    if (dataMap.get(i).size() < 4) {
                        break loop;
                    } else {
                        DeskInfo deskInfo = new DeskInfo();
                        deskInfo.setOrderInfouid(orderUid);
                        deskInfo.setLength(null == dataMap.get(i).get(2) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(2)));
                        deskInfo.setWidth(null == dataMap.get(i).get(4) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(4)));
                        deskInfo.setHeight(null == dataMap.get(i).get(6) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(6)));
                        String materialColor = null == dataMap.get(i).get(7) ? "无颜色" : dataMap.get(i).get(7);
                        deskInfo.setMaterialColor(MaterialColorEnums.getByName(materialColor));
                        deskInfo.setDeskNum(Integer.valueOf(null == dataMap.get(i).get(8) ? "0" : dataMap.get(i).get(8).replace(".0", "")));
                        deskInfo.setPrice(null == dataMap.get(i).get(10) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(10)).setScale(0, BigDecimal.ROUND_HALF_DOWN));
                        deskInfo.setTotalPrice(null == dataMap.get(i).get(11) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(11)).setScale(0, BigDecimal.ROUND_HALF_DOWN));
                        deskInfo.setRemark(null == dataMap.get(i).get(12) ? "无颜色" : dataMap.get(i).get(12));
                        deskInfos.add(deskInfo);
                    }
                }

            }
            if (null != dataMap.get(rowNum).get(2) && dataMap.get(rowNum).get(2).replaceAll("\r|\n", "").contains("产品名称")) {

                //配件
                loop:
                for (int i = rowNum + 1; i < dataMap.size(); i++) {
                    if (dataMap.get(i).size() < 4) {
                        break loop;
                    } else {
                        IronwareInfo ironwareInfo = new IronwareInfo();
                        ironwareInfo.setOrderInfouid(orderUid);
                        ironwareInfo.setIronwareName(null == dataMap.get(i).get(2) ? "" : dataMap.get(i).get(2));
                        ironwareInfo.setIronwareColor(null == dataMap.get(i).get(6) ? "" : dataMap.get(i).get(6));
                        ironwareInfo.setSpecification(null == dataMap.get(i).get(6) ? "" : dataMap.get(i).get(6));
                        ironwareInfo.setIronwareNum(Integer.valueOf(null == dataMap.get(i).get(8) ? "0" : dataMap.get(i).get(8).replace(".0", "")));
                        ironwareInfo.setPrice(null == dataMap.get(i).get(10) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(10)).setScale(0, BigDecimal.ROUND_HALF_DOWN));
                        ironwareInfo.setTotalPrice(null == dataMap.get(i).get(11) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(11)).setScale(0, BigDecimal.ROUND_HALF_DOWN));
                        ironwareInfo.setRemark(null == dataMap.get(i).get(12) ? "" : dataMap.get(i).get(12));
                        ironwareInfos.add(ironwareInfo);
                    }
                }
            }

        }

        result.put("desks", deskInfos);
        result.put("irons", ironwareInfos);
        return result;


    }


    public Map<String, Object> analyzeMetal(Map<Integer, Map<Integer, String>> dataMap, String orderUid) {

        Map<String, Object> result = new HashMap<>();

        List<MetalInfo> metalInfos = new ArrayList<>();
        List<IronwareInfo> ironwareInfos = new ArrayList<>();

        for (Integer rowNum : dataMap.keySet()) {
            if (null != dataMap.get(rowNum).get(1) && dataMap.get(rowNum).get(1).replaceAll("\r|\n", "").contains("型号名称")) {
                //材料单
                loop:
                for (int i = rowNum + 1; i < dataMap.size(); i++) {

                    if (dataMap.get(i).size() < 6) {
                        break loop;
                    } else {
                        MetalInfo metalInfo = new MetalInfo();
                        metalInfo.setOrderInfouid(orderUid);
                        metalInfo.setMetalName(null == dataMap.get(i).get(1) ? "" : dataMap.get(i).get(1));
                        String materialColor = null == dataMap.get(i).get(2) ? "" : dataMap.get(i).get(2);
                        metalInfo.setMaterialColor(MaterialColorEnums.getByName(materialColor));
                        metalInfo.setSpecification(null == dataMap.get(i).get(4) ? "" : dataMap.get(i).get(4));
                        metalInfo.setMetalNum(null == dataMap.get(i).get(5) ? 0 : Integer.valueOf(dataMap.get(i).get(5).replace(".0", "")));
                        metalInfo.setPrice(null == dataMap.get(i).get(6) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(6)));
                        metalInfo.setTotalPrice(null == dataMap.get(i).get(7) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(7)));
                        metalInfo.setRemark(null == dataMap.get(i).get(8) ? "" : dataMap.get(i).get(8));
                        metalInfos.add(metalInfo);
                    }

                }

            }
            if (null != dataMap.get(rowNum).get(1) && dataMap.get(rowNum).get(1).replaceAll("\r|\n", "").contains("配件名称")) {
                //材料单
                loop1:
                for (int i = rowNum + 1; i < dataMap.size(); i++) {

                    if (dataMap.get(i).size() < 3) {
                        break loop1;
                    } else {
                        IronwareInfo ironwareInfo = new IronwareInfo();
                        ironwareInfo.setOrderInfouid(orderUid);
                        ironwareInfo.setIronwareName(null == dataMap.get(i).get(1) ? "" : dataMap.get(i).get(1));
                        ironwareInfo.setIronwareColor(null == dataMap.get(i).get(2) ? "" : dataMap.get(i).get(2));
                        ironwareInfo.setSpecification(null == dataMap.get(i).get(4) ? "" : dataMap.get(i).get(4));
                        ironwareInfo.setIronwareNum(null == dataMap.get(i).get(5) ? 0 : Integer.valueOf(dataMap.get(i).get(5).replace(".0", "")));
                        ironwareInfo.setPrice(null == dataMap.get(i).get(6) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(6)));
                        ironwareInfo.setTotalPrice(null == dataMap.get(i).get(7) ? new BigDecimal("0") : new BigDecimal(dataMap.get(i).get(7)));
                        ironwareInfo.setRemark(null == dataMap.get(i).get(8) ? "" : dataMap.get(i).get(8));
                        ironwareInfos.add(ironwareInfo);
                    }
                }
            }
        }

        result.put("metals", metalInfos);
        result.put("irons", ironwareInfos);
        System.out.println(metalInfos.size() + "==" + ironwareInfos.size());
        return result;

    }


}
