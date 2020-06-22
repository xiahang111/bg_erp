package com.bingo.erp.xo.order.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.erp.base.enums.CustomerResourceEnums;
import com.bingo.erp.base.enums.OrderStatusEnums;
import com.bingo.erp.base.enums.OrderTypeEnums;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.base.vo.CustomerVO;
import com.bingo.erp.commons.entity.Admin;
import com.bingo.erp.commons.entity.IronwareInfo;
import com.bingo.erp.commons.entity.MaterialInfo;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.commons.feign.PersonFeignClient;
import com.bingo.erp.utils.DateUtils;
import com.bingo.erp.utils.JsonUtils;
import com.bingo.erp.utils.RedisUtil;
import com.bingo.erp.xo.order.global.ExcelConf;
import com.bingo.erp.xo.order.global.NormalConf;
import com.bingo.erp.xo.order.global.RedisConf;
import com.bingo.erp.xo.order.global.SysConf;
import com.bingo.erp.xo.order.mapper.*;
import com.bingo.erp.xo.order.service.OrderService;
import com.bingo.erp.xo.order.tools.CBDOrderTools;
import com.bingo.erp.xo.order.tools.OrderTools;
import com.bingo.erp.xo.order.vo.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

@Service
@Slf4j
public class OrderServiceImpl extends SuperServiceImpl<OrderInfoMapper, OrderInfo> implements OrderService {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private TransomMapper transomMapper;

    @Resource
    private MaterialInfoMapper materialInfoMapper;

    @Resource
    private IronwareInfoMapper ironwareInfoMapper;

    @Resource
    private LaminaterInfoMapper laminaterInfoMapper;

    @Resource
    private OrderService orderService;

    @Resource
    OrderTools tools;

    @Resource
    CBDOrderTools cbdOrderTools;

    @Autowired
    RedisUtil redisUtil;

    @Resource
    private PersonFeignClient personFeignClient;

    @Value(value = "${srcFileUrl}")
    private String SRC_FILE_URL;

    @Value(value = "${newFileDict}")
    private String NEW_FILE_DICT;


    @Override
    @DS("slave")
    public List<IndexOrderVO> getIndexOrderInfo() {

        List<IndexOrderVO> result = new ArrayList<>();
        List<OrderInfo> infos = orderInfoMapper.getIndexOrderInfo();

        for (OrderInfo info : infos) {
            IndexOrderVO vo = new IndexOrderVO(info.getOrderId(), info.getTotalPrice(), info.getSalesman(), info.getStatus());
            result.add(vo);
        }

        return result;
    }


    @Override
    public List<String> saveCBDOrder(String adminUid, LaminateVO laminateVO) throws Exception {

        log.info("===============方法开始，参数信息：excel文件夹：" + NEW_FILE_DICT);

        List<String> result = new ArrayList<>();

        String suffix = DateUtils.formateDate(new Date(), DateUtils.DAYFORMAT_STRING) + RandomUtil.randomNumbers(6) + ExcelConf.FILE_SUFFIX;

        String fileName = "层板订单" + suffix;

        result.add(fileName);


        //确定表格结构
        if (null == laminateVO.getLaminateInfos() || laminateVO.getLaminateInfos().size() <= 0) {
            throw new MessageException("没有填写层板灯信息哦~请填写");
        }

        if (null == laminateVO.getIronwares()) {
            laminateVO.setIronwares(new ArrayList<>());
        }

        tools.ironwareCalculate(laminateVO.getIronwares());
        cbdOrderTools.laminateCalculate(laminateVO.getLaminateInfos());
        cbdOrderTools.orderCalculate(laminateVO);

        String orderFileName = SRC_FILE_URL + ExcelConf.CBD_ORDER_FILENAME;
        String productSrcFileName = SRC_FILE_URL + ExcelConf.CBD_PRODUCT_ORDER_FILENAME;

        POIFSFileSystem fs = new POIFSFileSystem(new File(orderFileName));

        HSSFWorkbook wb = new HSSFWorkbook(fs);

        HSSFSheet sheet = wb.getSheetAt(0);

        int addnum = cbdOrderTools.extensionExcel(sheet, laminateVO);

        cbdOrderTools.fillData(sheet, laminateVO, addnum);

        File newFile = new File(NEW_FILE_DICT + fileName);

        wb.write(newFile);

        Map<String, Object> dataMap = cbdOrderTools.toMap(laminateVO);

        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(NEW_FILE_DICT + fileName, dataMap, NEW_FILE_DICT + fileName);

        result.add(fileName);

        /**
         * ============================================生产单制作==============================================================================================
         */

        String productFileName = "层板生产单" + suffix;

        File productFile = new File(NEW_FILE_DICT + productFileName);

        POIFSFileSystem productFs = new POIFSFileSystem(new File(productSrcFileName));

        HSSFWorkbook productWb = new HSSFWorkbook(productFs);

        HSSFSheet productSheet = productWb.getSheetAt(0);

        int addnum1 = cbdOrderTools.productExtensionExcel(productSheet, laminateVO);

        cbdOrderTools.productFillData(productSheet, addnum1, laminateVO);

        productWb.write(productFile);

        Thread.sleep(1000l);

        transformer.transformXLS(NEW_FILE_DICT + productFileName, dataMap, NEW_FILE_DICT + productFileName);

        result.add(productFileName);

        //=====================================================存入数据库================================================================================================================


        OrderInfo orderInfo = tools.getOrderInfo(laminateVO);

        laminateVO.setOrderType(OrderTypeEnums.CBDORDER.code);
        orderInfo.setOrderType(OrderTypeEnums.CBDORDER);

        orderInfo.setAdminUid(adminUid);

        String roleName = tools.getRoleNameByAdminUid(adminUid);

        if (SysConf.CLERK_ROLE.equals(roleName)) {
            laminateVO.setOrderStatus(OrderStatusEnums.STAY_CONFIRM.code);
            orderInfo.setOrderStatus(OrderStatusEnums.STAY_CONFIRM);
        } else {
            laminateVO.setOrderStatus(OrderStatusEnums.STAY_CONFIRM.code);
            orderInfo.setOrderStatus(OrderStatusEnums.STAY_CONFIRM);
        }

        orderInfoMapper.insert(orderInfo);

        cbdOrderTools.saveLaminateInfoList(laminaterInfoMapper, orderInfo.getUid(), laminateVO.getLaminateInfos());

        tools.saveIronwareInfoList(ironwareInfoMapper, laminateVO.getIronwares(), orderInfo.getUid());

        tools.saveCustomer(adminUid, laminateVO, personFeignClient);

        redisUtil.set(RedisConf.ORDER_UID_PRE + orderInfo.getUid(), JsonUtils.objectToJson(laminateVO));

        return result;
    }

    @Override
    public List<String> saveOrder(String adminUid, MaterialVO materialVO) throws Exception {

        log.info("===============方法开始，参数信息：excel文件夹：" + NEW_FILE_DICT);

        List<String> result = new ArrayList<>();

        String suffix = DateUtils.formateDate(new Date(), DateUtils.DAYFORMAT_STRING) + RandomUtil.randomNumbers(6) + ExcelConf.FILE_SUFFIX;

        String fileName = "订单" + suffix;

        result.add(fileName);


        //确定表格结构
        if (null == materialVO.getMaterials() || materialVO.getMaterials().size() <= 0) {
            throw new MessageException("没有填写材玻信息哦~请填写");
        }

        if (null == materialVO.getIronwares()) {
            materialVO.setIronwares(new ArrayList<>());
        }

        /*if (!materialValidate(materialVO.getMaterials())) {
            throw new MessageException("要保持料型号颜色、拉手类型和玻璃颜色一致哦~");
        }*/

        int ironwareNum = materialVO.getIronwares().size() - 1;

        int transomNum = 0;

        //根据数量扩充表格

        //计算数据

        tools.ironwareCalculate(materialVO.getIronwares());
        tools.materialCalculate(materialVO.getMaterials());
        if (materialVO.isHaveTransom && null != materialVO.getTransoms()) {
            transomNum = materialVO.getTransoms().size() - 1;
            tools.transomCalculate(materialVO.getTransoms());
        }

        tools.orderCalculate(materialVO);

        try {
            String orderFileName = "";
            String productSrcFileName = "";

            if (materialVO.isHaveTransom && null != materialVO.getTransoms() && materialVO.getTransoms().size() > 0) {
                orderFileName = SRC_FILE_URL + ExcelConf.TDHL_ORDER_FILENAME;
                productSrcFileName = SRC_FILE_URL + ExcelConf.TDHL_PRODUCT_ORDER_FILENAME;
            } else {
                orderFileName = SRC_FILE_URL + ExcelConf.ORDER_FILENAME;
                productSrcFileName = SRC_FILE_URL + ExcelConf.PRODUCT_ORDER_FILENAME;

            }

            POIFSFileSystem fs = new POIFSFileSystem(new File(orderFileName));

            HSSFWorkbook wb = new HSSFWorkbook(fs);

            HSSFSheet sheet = wb.getSheetAt(0);

            Map<String, List<MaterialInfoVO>> map = tools.materialsToMap(materialVO.getMaterials());

            int addnum = tools.extensionExcel(sheet, map, ironwareNum, transomNum, materialVO.isHaveTransom);

            //填充料玻、五金数据
            tools.fillData(sheet, map, materialVO, materialVO.getIronwares(), addnum);

            File newFile = new File(NEW_FILE_DICT + fileName);

            wb.write(newFile);

            Map<String, Object> dataMap = tools.toMap(materialVO);

            XLSTransformer transformer = new XLSTransformer();
            transformer.transformXLS(NEW_FILE_DICT + fileName, dataMap, NEW_FILE_DICT + fileName);

            /**
             =========================== 生产单制作 ================================
             */

            String productFileName = "生产单" + suffix;

            File productFile = new File(NEW_FILE_DICT + productFileName);

            POIFSFileSystem productFs = new POIFSFileSystem(new File(productSrcFileName));

            HSSFWorkbook productWb = new HSSFWorkbook(productFs);

            HSSFSheet productSheet = productWb.getSheetAt(0);

            int addnum1 = tools.productExtensionExcel(productSheet, map, materialVO);

            tools.productFillData(productSheet, addnum1, map, materialVO, materialVO.getIronwares());

            productWb.write(productFile);

            Thread.sleep(1000l);

            transformer.transformXLS(NEW_FILE_DICT + productFileName, dataMap, NEW_FILE_DICT + productFileName);

            result.add(productFileName);

            /**
             * ==========================================存入数据库 ======================================
             */

            OrderInfo orderInfo = tools.getOrderInfo(materialVO);

            orderInfo.setOrderType(OrderTypeEnums.DOORORDER);
            materialVO.setOrderType(OrderTypeEnums.DOORORDER.code);
            orderInfo.setAdminUid(adminUid);

            String roleName = tools.getRoleNameByAdminUid(adminUid);

            if (SysConf.CLERK_ROLE.equals(roleName)) {
                materialVO.setOrderStatus(OrderStatusEnums.STAY_CONFIRM.code);
                orderInfo.setOrderStatus(OrderStatusEnums.STAY_CONFIRM);
            } else {
                materialVO.setOrderStatus(OrderStatusEnums.STAY_CONFIRM.code);
                orderInfo.setOrderStatus(OrderStatusEnums.STAY_CONFIRM);
            }

            orderInfoMapper.insert(orderInfo);

            tools.saveMaterialInfoList(materialInfoMapper, orderInfo.getUid(), materialVO.getMaterials());

            tools.saveIronwareInfoList(ironwareInfoMapper, materialVO.getIronwares(), orderInfo.getUid());

            if (materialVO.isHaveTransom && null != materialVO.getTransoms() && materialVO.getTransoms().size() > 0) {

                tools.saveTransomInfoList(transomMapper, materialVO.transoms, orderInfo.getUid());
            }

            //保存客户信息
            tools.saveCustomer(adminUid, materialVO, personFeignClient);


            //将订单信息放到redis中
            log.info("主键id" + orderInfo.getUid());

            redisUtil.set(RedisConf.ORDER_UID_PRE + orderInfo.getUid(), JsonUtils.objectToJson(materialVO));

            return result;

        } catch (Exception e) {

            e.printStackTrace();
            throw new MessageException("生成表格失败！");
        }


    }

    @Override
    @DS("slave")
    public IPage<OrderInfo> getMaterialVOByUser(Admin admin, OrderRecordPageVO orderRecordPageVO) {

        String adminUid = admin.getUid();
        String username = admin.getUserName();

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        if (username.equals("admin")) {
            queryWrapper.lt("create_time", new Date());
        } else {
            queryWrapper.eq("admin_uid", adminUid);
        }
        queryWrapper.orderByDesc("create_time");

        //分页查询
        Page<OrderInfo> page = new Page<>();
        page.setCurrent(orderRecordPageVO.getCurrentPage());
        page.setSize(orderRecordPageVO.getPageSize());

        IPage<OrderInfo> orderInfoPage = orderService.page(page, queryWrapper);

        return orderInfoPage;
    }

    @Override
    @DS("slave")
    public ProductVO getMaterialVOByUid(String uid) {

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("uid", uid);


        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);

        List<MaterialInfo> materialInfos = materialInfoMapper.getAllByOrderUid(uid);

        List<IronwareInfo> ironwareInfos = ironwareInfoMapper.getAllByOrderUid(uid);

        MaterialVO materialVO = tools.revertToMaterialVO(orderInfo, materialInfos, ironwareInfos);


        return materialVO;
    }

    @Override
    public Map<String, Object> saveOrderAgain(String adminUid, String orderUid) {

        String orderJson = redisUtil.get(RedisConf.ORDER_UID_PRE + orderUid);

        ProductVO productVO = (ProductVO) JsonUtils.jsonToObject(orderJson, ProductVO.class);

        Map<String, Object> result = new HashMap<>();

        if (OrderTypeEnums.DOORORDER.code == productVO.getOrderType()) {
            productVO = (MaterialVO) JsonUtils.jsonToObject(orderJson, MaterialVO.class);
        } else {
            productVO = (LaminateVO) JsonUtils.jsonToObject(orderJson, LaminateVO.class);
        }
        result.put("orderType", productVO.getOrderType());
        result.put("productVO", productVO);

        return result;
    }
}














