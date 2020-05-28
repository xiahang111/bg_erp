package com.bingo.erp.xo.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.db.sql.Order;
import com.bingo.erp.base.enums.*;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.fatocry.material.MaterialCalculateFactory;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.IronwareInfo;
import com.bingo.erp.commons.entity.MaterialInfo;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.commons.entity.Product;
import com.bingo.erp.utils.DateUtils;
import com.bingo.erp.xo.enums.MaterialFactoryEnum;
import com.bingo.erp.xo.global.ExcelConf;
import com.bingo.erp.xo.global.NormalConf;
import com.bingo.erp.xo.mapper.IronwareInfoMapper;
import com.bingo.erp.xo.mapper.MaterialInfoMapper;
import com.bingo.erp.xo.mapper.OrderInfoMapper;
import com.bingo.erp.xo.mapper.ProductMapper;
import com.bingo.erp.xo.service.OrderService;
import com.bingo.erp.xo.vo.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.jxls.transformer.XLSTransformer;
import net.sf.jxls.util.Util;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class OrderServiceImpl extends SuperServiceImpl<OrderInfoMapper, OrderInfo> implements OrderService {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private MaterialInfoMapper materialInfoMapper;

    @Resource
    private IronwareInfoMapper ironwareInfoMapper;

    @Override
    public List<String> saveOrder(MaterialVO materialVO) throws Exception {

        List<String> result = new ArrayList<>();

        String suffix = DateUtils.formateDate(new Date(), DateUtils.DAYFORMAT_STRING) + RandomUtil.randomNumbers(6) + ExcelConf.FILE_SUFFIX;

        String fileName = "订单" + suffix;

        result.add(fileName);


        //确定表格结构
        if (null == materialVO.getMaterials() || materialVO.getMaterials().size() <= 0) {
            throw new MessageException("没有填写材玻信息哦~请填写");
        }

        if (null == materialVO.getIronwares() || materialVO.getIronwares().size() <= 0) {
            throw new MessageException("没有填写五金信息哦~请填写");
        }

        if (!materialValidate(materialVO.getMaterials())) {
            throw new MessageException("要保持料型号颜色、拉手类型和玻璃颜色一致哦~");
        }

        int materialNum = materialVO.getMaterials().size() - 1;

        int ironwareNum = materialVO.getIronwares().size() - 1;

        //根据数量扩充表格

        //计算数据
        materialValidate(materialVO.getMaterials());
        ironwareCalculate(materialVO.getIronwares());
        materialCalculate(materialVO.getMaterials());
        orderCalculate(materialVO);

        try {

            POIFSFileSystem fs = new POIFSFileSystem(new File(ExcelConf.SRC_FILE_URL + ExcelConf.ORDER_FILENAME));

            HSSFWorkbook wb = new HSSFWorkbook(fs);

            HSSFSheet sheet = wb.getSheetAt(0);

            int ironwareStartNum = ExcelConf.IRONWARE_START_NUM + materialNum;

            extensionExcel(sheet, materialNum, ironwareNum, ExcelConf.MATERIAL_START_NUM, ironwareStartNum);

            //填充料玻、五金数据
            fillData(sheet, materialVO.getMaterials(), materialVO.getIronwares());

            File newFile = new File(ExcelConf.NEW_FILE_DICT + fileName);

            wb.write(newFile);

            Map<String, Object> dataMap = toMap(materialVO);

            XLSTransformer transformer = new XLSTransformer();
            transformer.transformXLS(ExcelConf.NEW_FILE_DICT + fileName, dataMap, ExcelConf.NEW_FILE_DICT + fileName);

            /**
             =========================== 生产单制作 ================================
             */

            String productFileName = "生产单" + suffix;

            File productFile = new File(ExcelConf.NEW_FILE_DICT + productFileName);

            POIFSFileSystem productFs = new POIFSFileSystem(new File(ExcelConf.SRC_FILE_URL + ExcelConf.PRODUCT_ORDER_FILENAME));

            HSSFWorkbook productWb = new HSSFWorkbook(productFs);

            HSSFSheet productSheet = productWb.getSheetAt(0);

            int productIronwareStartNum = ExcelConf.PRODUCT_IRONWARE_START_NUM + materialNum;

            productExtensionExcel(productSheet, materialNum, ironwareNum);

            productFillData(productSheet, materialVO.getMaterials(), materialVO.getIronwares());

            productWb.write(productFile);

            Thread.sleep(1000l);

            transformer.transformXLS(ExcelConf.NEW_FILE_DICT + productFileName, dataMap, ExcelConf.NEW_FILE_DICT + productFileName);

            result.add(productFileName);

            /**
             * ==========================================存入数据库 ======================================
             */

            OrderInfo orderInfo = getOrderInfo(materialVO);

            orderInfoMapper.insert(orderInfo);

            saveMaterialInfoList(materialInfoMapper,orderInfo.getUid(), materialVO.getMaterials());

            saveIronwareInfoList(ironwareInfoMapper,materialVO.getIronwares(), orderInfo.getUid());

            log.info("主键id" + orderInfo.getUid());

            return result;

        } catch (Exception e) {

            e.printStackTrace();
            throw new MessageException("生成表格失败！");
        }


    }


    private boolean materialValidate(List<MaterialInfoVO> materialInfoVOS) {

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

    private void extensionExcel(HSSFSheet sheet, int materialNum, int ironwareNum, int materialStartNum, int ironwareStartNum) {


        if (materialNum > 0) {
            sheet.shiftRows(materialStartNum, sheet.getLastRowNum(), materialNum, true, false);

            for (int i = 0; i < materialNum; i++) {
                Util.copyRow(sheet, sheet.getRow(materialStartNum - 1), sheet.getRow(materialStartNum + i));
            }
        }

        if (ironwareNum > 0) {
            sheet.shiftRows(ironwareStartNum, sheet.getLastRowNum(), ironwareNum, true, false);

            for (int i = 0; i < ironwareNum; i++) {
                Util.copyRow(sheet, sheet.getRow(ironwareStartNum - 1), sheet.getRow(ironwareStartNum + i));
            }
        }


    }

    private void productExtensionExcel(HSSFSheet sheet, int materialNum, int ironwareNum) {
        int MATERIAL_START_NUM = ExcelConf.MATERIAL_START_NUM;
        int GLASS_START_NUM = ExcelConf.GLASS_START_NUM + materialNum;

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
    }

    /**
     * 料玻信息计算
     *
     * @param materialInfoVOS
     * @throws Exception
     */
    private void materialCalculate(List<MaterialInfoVO> materialInfoVOS) throws Exception {


        for (MaterialInfoVO materialInfoVO : materialInfoVOS) {
            //todo 计算玻璃和料型

            //获取产品ID
            int materialType = materialInfoVO.getMaterialType();

            MaterialCalculateFactory factory = (MaterialCalculateFactory) MaterialFactoryEnum.getFactoryClass(Integer.valueOf(materialType)).newInstance();
            MaterialCalculateResultVO vo = (MaterialCalculateResultVO) factory.calculate(materialInfoVO);

            materialInfoVO.setMaterialHeight(vo.getMaterialHeight());
            materialInfoVO.setMaterialWidth(vo.getMaterialWidth());

            materialInfoVO.setGlassHeight(vo.getGlassHeight());
            materialInfoVO.setGlassWidth(vo.getGlassWidth());

            materialInfoVO.setMaterialDetail(vo.getMaterialDetail());

            materialInfoVO.setArea(
                    materialInfoVO.getHeight().
                            multiply(materialInfoVO.getWidth()).
                            multiply(new BigDecimal(materialInfoVO.getMaterialNum())).divide(NormalConf.divideNum).setScale(2, BigDecimal.ROUND_CEILING)
            );

            materialInfoVO.setTotalPrice(materialInfoVO.area.multiply(materialInfoVO.getPrice()).setScale(2));

        }


    }

    /**
     * 计算订单表内五金信息需要计算的内容
     *
     * @param ironwareInfoVOS
     * @throws Exception
     */
    private void ironwareCalculate(List<IronwareInfoVO> ironwareInfoVOS) throws Exception {

        ironwareInfoVOS.stream().forEach(ironwareInfoVO -> {
            ironwareInfoVO.setTotalPrice(ironwareInfoVO.getPrice().
                    multiply(new BigDecimal(ironwareInfoVO.getIronwareNum()).setScale(2)));
        });
    }

    /**
     * 计算整个订单表需要计算的内容
     *
     * @param materialVO
     */
    private void orderCalculate(MaterialVO materialVO) {

        BigDecimal materialTotalPrice = new BigDecimal("0");


        for (MaterialInfoVO materialInfoVO : materialVO.getMaterials()) {

            materialTotalPrice = materialTotalPrice.add(materialInfoVO.getTotalPrice());

        }

        BigDecimal ironwareTotalPrice = new BigDecimal("0");


        for (IronwareInfoVO ironwareInfoVO : materialVO.getIronwares()) {

            ironwareTotalPrice = ironwareTotalPrice.add(ironwareInfoVO.getTotalPrice());

        }

        BigDecimal packageTotalPrice = NormalConf.BIG_PACKAGE_PRICE.
                multiply(new BigDecimal(materialVO.getBigPackageNum())).
                add(NormalConf.SIMPLE_PACKAGE_PRICE.multiply(new BigDecimal(materialVO.getSimplePackageNum())));


        materialVO.setIronTotalPrice(ironwareTotalPrice.add(packageTotalPrice));

        materialVO.setMaterialTotalprice(materialTotalPrice);

        materialVO.setOrderTotalPrice(materialTotalPrice.add(materialVO.getIronTotalPrice()));

    }

    private Map<String, Object> toMap(MaterialVO materialVO) {

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("orderId", materialVO.getOrderId());
        dataMap.put("customerNick", materialVO.getCustomerNick());
        dataMap.put("orderDate", materialVO.getOrderDate());
        dataMap.put("deliveryDate", materialVO.getDeliveryDate());
        dataMap.put("orderMaker", materialVO.getOrderMaker());
        dataMap.put("salesman", materialVO.getSalesman());
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
        dataMap.put("materialTotalPrice", materialVO.getMaterialTotalprice());
        dataMap.put("orderRemark", materialVO.getRemark());
        dataMap.put("customerName", materialVO.getCustomerName());
        dataMap.put("customerPhoneNum", materialVO.getCustomerPhoneNum());
        dataMap.put("customerAddr", materialVO.getCustomerAddr());

        return dataMap;
    }


    private void fillData(HSSFSheet sheet, List<MaterialInfoVO> materialInfoVOS, List<IronwareInfoVO> ironwareInfoVOS) {

        Map<Integer, String> letMap = new HashMap<>();
        letMap.put(0, "A");
        letMap.put(1, "B");
        letMap.put(2, "C");
        letMap.put(3, "D");
        letMap.put(4, "E");

        for (int i = 0; i < materialInfoVOS.size(); i++) {

            MaterialInfoVO materialInfoVO0 = materialInfoVOS.get(i);

            HSSFRow row = sheet.getRow(ExcelConf.MATERIAL_START_NUM - 1 + i);

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
                    cell.setCellValue(DirectionsEnums.getEnumByCode(materialInfoVO0.getDirection()));
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

        }

        for (int l = 0; l < ironwareInfoVOS.size(); l++) {

            IronwareInfoVO ironwareInfoVO0 = ironwareInfoVOS.get(l);

            HSSFRow row = sheet.getRow(ExcelConf.IRONWARE_START_NUM - 1 + materialInfoVOS.size() - 1 + l);

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

                if (m == 6) {
                    cell.setCellValue(ironwareInfoVO0.getIronwareNum());
                }

                if (m == 7) {
                    cell.setCellValue(ironwareInfoVO0.getPrice() + "");
                }

                if (m == 8) {
                    cell.setCellValue(ironwareInfoVO0.getTotalPrice() + "");
                }

                if (m == 9) {
                    cell.setCellValue(ironwareInfoVO0.getRemark() + "");
                }
            }

        }

    }

    private void productFillData(HSSFSheet sheet, List<MaterialInfoVO> materialInfoVOS, List<IronwareInfoVO> ironwareInfoVOS) {

        int materialNum = materialInfoVOS.size() - 1;

        int GLASS_START_NUM = 13 + materialNum;

        int IRONWARE_START_NUM = 16 + materialNum * 2;

        Map<Integer, String> letMap = new HashMap<>();
        letMap.put(0, "A");
        letMap.put(1, "B");
        letMap.put(2, "C");
        letMap.put(3, "D");
        letMap.put(4, "E");

        for (int a = 0; a < materialInfoVOS.size(); a++) {

            MaterialInfoVO infoVO = materialInfoVOS.get(a);

            HSSFRow materialRow = sheet.getRow(ExcelConf.MATERIAL_START_NUM - 1 + a);
            HSSFRow glassRow = sheet.getRow(GLASS_START_NUM - 1 + a);

            for (int b = 0; b < materialRow.getLastCellNum(); b++) {
                HSSFCell cell = materialRow.getCell(b);

                if (b == 0) {
                    cell.setCellValue(letMap.get(a));
                }

                if (b == 1) {
                    cell.setCellValue(infoVO.getHeight() + "");
                }

                if (b == 2) {
                    cell.setCellValue(infoVO.getWidth() + "");
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
                    cell.setCellValue(DirectionsEnums.getEnumByCode(infoVO.getDirection()));
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


        //==========填充五金信息===========

        for (int d = 0; d < ironwareInfoVOS.size(); d++) {

            IronwareInfoVO infoVO = ironwareInfoVOS.get(d);

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
                    cell.setCellValue(IronwareColorEnums.getEnumByCode(infoVO.getIronwareColor()));
                }
                if (e == 6) {
                    cell.setCellValue(infoVO.getSpecification());
                }
                if (e == 7) {
                    cell.setCellValue(infoVO.getRemark());
                }

            }

        }

    }

    private OrderInfo getOrderInfo(MaterialVO materialVO) {

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
                materialVO.getOrderTotalPrice());

        return info;

    }

    private void saveMaterialInfoList(MaterialInfoMapper mapper, String uid, List<MaterialInfoVO> materialInfoVOS) {

        for (MaterialInfoVO materialInfoVO : materialInfoVOS) {

            MaterialInfo info = new MaterialInfo(uid, MaterialColorEnums.getByCode(materialInfoVO.getGlassColor()),
                    MaterialTypeEnums.getByCode(materialInfoVO.getMaterialType()),
                    HandleEnums.getByCode(materialInfoVO.getHandleType()),
                    materialInfoVO.getHingeLocation(), GlassColor.getByCode(materialInfoVO.getGlassColor()),
                    materialInfoVO.getGlassHeight(), materialInfoVO.getGlassWidth(),
                    materialInfoVO.getHeight(), materialInfoVO.getWidth(),
                    materialInfoVO.getMaterialHeight(), materialInfoVO.getMaterialWidth(),
                    materialInfoVO.getMaterialNum(), materialInfoVO.getHandlePlace(),
                    DirectionsEnums.getByCode(materialInfoVO.getDirection()),
                    materialInfoVO.getMaterialDetail(), materialInfoVO.getRemark(),
                    materialInfoVO.getPrice(), materialInfoVO.getArea(), materialInfoVO.getTotalPrice());
            mapper.insert(info);

        }


    }

    public void saveIronwareInfoList(IronwareInfoMapper mapper, List<IronwareInfoVO> ironwareInfoVOS, String uid) {

        for (IronwareInfoVO ironwareInfoVO : ironwareInfoVOS) {

            IronwareInfo info = new IronwareInfo(uid, ironwareInfoVO.getIronwareName(), ironwareInfoVO.getUnit(), IronwareColorEnums.getEnumByCode(ironwareInfoVO.getIronwareColor()),
                    ironwareInfoVO.getSpecification(), ironwareInfoVO.getIronwareNum(), ironwareInfoVO.getPrice(),
                    ironwareInfoVO.getRemark(), ironwareInfoVO.getTotalPrice());

            mapper.insert(info);
        }

    }
}
