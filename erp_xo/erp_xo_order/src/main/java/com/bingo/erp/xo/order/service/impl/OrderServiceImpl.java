package com.bingo.erp.xo.order.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.erp.base.enums.*;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.*;
import com.bingo.erp.commons.feign.PersonFeignClient;
import com.bingo.erp.utils.DateUtils;
import com.bingo.erp.utils.JsonUtils;
import com.bingo.erp.utils.RedisUtil;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.order.global.ExcelConf;
import com.bingo.erp.xo.order.global.RedisConf;
import com.bingo.erp.xo.order.global.SysConf;
import com.bingo.erp.xo.order.mapper.*;
import com.bingo.erp.xo.order.service.*;
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
import org.springframework.transaction.annotation.Transactional;

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
    private MetalInfoMapper metalInfoMapper;

    @Resource
    private DeskInfoMapper deskInfoMapper;

    @Resource
    private MetalInfoService metalInfoService;

    @Resource
    private DeskInfoService deskInfoService;

    @Resource
    private MaterialInfoService materialInfoService;

    @Resource
    private IronwareInfoMapper ironwareInfoMapper;

    @Resource
    private IronwareInforService ironwareInforService;

    @Resource
    private LaminaterInfoMapper laminaterInfoMapper;

    @Resource
    private LaminateInfoService laminateInfoService;

    @Resource
    private OrderService orderService;

    @Resource
    OrderTools tools;

    @Resource
    CBDOrderTools cbdOrderTools;

    @Resource
    private OrderGlassDetailService orderGlassDetailService;

    @Resource
    private OrderGlassDetailMapper orderGlassDetailMapper;

    @Autowired
    RedisUtil redisUtil;

    @Resource
    private OrderFileRecordService orderFileRecordService;

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
    @Transactional
    public synchronized List<String> saveCBDOrder(String adminUid, LaminateVO laminateVO) throws Exception {

        log.info("===============方法开始，参数信息：excel文件夹：" + NEW_FILE_DICT);

        List<String> result = new ArrayList<>();

        String suffix = DateUtils.formateDate(new Date(), DateUtils.DAYFORMAT_STRING) + RandomUtil.randomNumbers(6) + ExcelConf.FILE_SUFFIX;

        String fileName = "层板订单" + suffix;

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

        //半成品添加配件laminateVO.getProductType() == ProductTypeEnums.NotComplete.code
        //12月1日 半成品成品都要有
        //12月3日 成品沒有角码
        if (true){
            int totalNum = 0;
            for (LaminateInfoVO laminateInfo : laminateVO.getLaminateInfos()){
                totalNum += laminateInfo.getLaminateNum();
            }
            laminateVO.getIronwares().addAll(tools.getCBDIronByNotComplete(totalNum,laminateVO.getProductType()));
        }

        String orderFileName = SRC_FILE_URL + ExcelConf.CBD_ORDER_FILENAME;
        String productSrcFileName = SRC_FILE_URL + ExcelConf.CBD_PRODUCT_ORDER_FILENAME;

        POIFSFileSystem fs = new POIFSFileSystem(new File(orderFileName));

        HSSFWorkbook wb = new HSSFWorkbook(fs);

        HSSFSheet sheet = wb.getSheetAt(0);

        Map<String, List<LaminateInfoVO>> map = cbdOrderTools.laminateToMap(laminateVO.getLaminateInfos());

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

        if (SysConf.CLERK_ROLE.equals(roleName) || SysConf.ADMIN_ROLE.equals(roleName)) {
            laminateVO.setOrderStatus(OrderStatusEnums.STAY_CONFIRM.code);
            orderInfo.setOrderStatus(OrderStatusEnums.STAY_CONFIRM);
        } else {
            laminateVO.setOrderStatus(OrderStatusEnums.STAY_CONFIRM.code);
            orderInfo.setOrderStatus(OrderStatusEnums.STAY_CONFIRM);
        }

        orderInfoMapper.insert(orderInfo);

        //保存文件信息
        OrderFileRecord orderFileRecord = new OrderFileRecord();

        orderFileRecord.setFileName(getStringByList(result));

        orderFileRecord.setOrderUid(orderInfo.getUid());

        orderFileRecord.setOrderId(orderInfo.getOrderId());

        orderFileRecordService.save(orderFileRecord);

        List<LaminateInfoVO> insertList = new ArrayList<>();

        for (String key : map.keySet()) {
            insertList.addAll(map.get(key));
        }

        //保存层板灯信息
        List<LaminateInfo> laminateInfos = cbdOrderTools.saveLaminateInfoList(laminaterInfoMapper, orderInfo.getUid(), insertList);
        //保存玻璃信息 不保存半成品的
        if (orderInfo.getProductType() == ProductTypeEnums.Complete) {
            getOrderGlassDetail(laminateInfos,orderInfo,orderGlassDetailMapper);
        }

        tools.saveIronwareInfoList(ironwareInfoMapper, laminateVO.getIronwares(), orderInfo.getUid());

        tools.saveCustomer(adminUid, laminateVO, orderInfo);

        redisUtil.set(RedisConf.ORDER_UID_PRE + orderInfo.getUid(), JsonUtils.objectToJson(laminateVO));

        return result;
    }

    @Override
    public synchronized List<String> saveOrder(String adminUid, MaterialVO materialVO) throws Exception {

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


        int transomNum = 0;

        //根据数量扩充表格

        //计算数据

        tools.mountingCalculate(materialVO);
        tools.ironwareCalculate(materialVO.getIronwares());
        tools.materialCalculate(materialVO.getMaterials());
        if (materialVO.isHaveTransom && null != materialVO.getTransoms()) {
            transomNum = materialVO.getTransoms().size() - 1;
            tools.transomCalculate(materialVO.getTransoms());

            //添加螺丝配件信息
            List<IronwareInfoVO> transomIrons = tools.getIronByHeight(materialVO.getTransoms());
            if(transomIrons.size() > 0){
                materialVO.getIronwares().addAll(transomIrons);
            }
        }
        int ironwareNum = materialVO.getIronwares().size() - 1;

        //计算订单价格
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

            log.info("excel模板总行数:"+sheet.getLastRowNum());
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

            int addnum1 = tools. productExtensionExcel(productSheet, map, materialVO);

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

            List<MaterialInfoVO> insertList = new ArrayList<>();

            for (String key : map.keySet()) {
                insertList.addAll(map.get(key));
            }

            List<MaterialInfo> materialInfos = tools.saveMaterialInfoList(materialInfoMapper, orderInfo.getUid(), insertList);

            tools.saveIronwareInfoList(ironwareInfoMapper, materialVO.getIronwares(), orderInfo.getUid());

            if (materialVO.isHaveTransom && null != materialVO.getTransoms() && materialVO.getTransoms().size() > 0) {

                tools.saveTransomInfoList(transomMapper, materialVO.transoms, orderInfo.getUid());
            }

            //保存文件信息
            OrderFileRecord orderFileRecord = new OrderFileRecord();

            orderFileRecord.setFileName(getStringByList(result));

            orderFileRecord.setOrderUid(orderInfo.getUid());

            orderFileRecord.setOrderId(orderInfo.getOrderId());

            orderFileRecordService.save(orderFileRecord);

            //如果账号权限不是游客 那么才会保存相关订单信息
            if(!SysConf.VISIT_ROLE.equals(roleName)){
                //保存玻璃信息 不保存半成品的
                if (orderInfo.getProductType() == ProductTypeEnums.Complete) {
                    getOrderGlassDetailByMaterial(materialInfos,orderInfo,orderGlassDetailMapper);

                }
            }
            //保存客户信息
            tools.saveCustomer(adminUid, materialVO, orderInfo);


            //将订单信息放到redis中
            log.info("主键id" + orderInfo.getUid());

            redisUtil.set(RedisConf.ORDER_UID_PRE + orderInfo.getUid(), JsonUtils.objectToJson(materialVO));

            return result;

        } catch (Exception e) {

            e.printStackTrace();
            throw new MessageException("生成表格失败！原因：" + e.getMessage());
        }


    }

    private String getStringByList(List<String> list) {

        String result = "";

        if (CollectionUtil.isNotEmpty(list)) {
            for (String string : list) {
                result = result + string + "-";
            }
        }


        return result;

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
        queryWrapper.eq("status", SysConf.NORMAL_STATUS);

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
        MaterialVO materialVO = new MaterialVO();
        if (null != orderInfo){
            materialVO = tools.revertToMaterialVO(orderInfo, materialInfos, ironwareInfos);
        }

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

    @Override
    public List<String> getFileNamesByOrderUid(String orderUid) throws MessageException {

        if (StringUtils.isBlank(orderUid)) {
            throw new MessageException("传入订单uid为空！");
        }

        QueryWrapper<OrderFileRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_uid", orderUid);
        OrderFileRecord orderFileRecord = orderFileRecordService.getOne(queryWrapper);

        if (null == orderFileRecord) {
            throw new MessageException("库内无此订单文件信息！");
        }

        String files = orderFileRecord.getFileName();

        String[] filenames = files.split("-");

        List<String> result = new ArrayList<>();

        for (String s : filenames) {
            if (StringUtils.isNotBlank(s)) {
                result.add(s);
            }
        }

        return result;
    }

    @Override
    @Transactional
    public void deleteOrderById(String uid) throws Exception {

        if (StringUtils.isBlank(uid)) {
            throw new MessageException("传入订单uid为空！");
        }

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        OrderInfo orderInfo = orderService.getOne(queryWrapper);

        if (null == orderInfo) {
            throw new MessageException("库内无此订单文件信息！");
        }

        orderInfo.setStatus(2);

        orderInfoMapper.updateById(orderInfo);

        //将材料和五金状态设置为2

        List<MaterialInfo> materialInfos = materialInfoMapper.getAllByOrderUid(uid);

        List<IronwareInfo> ironwareInfos = ironwareInfoMapper.getAllByOrderUid(uid);

        //将材料单内容置空
        List<MetalInfo> metalInfos = metalInfoMapper.getAllByOrderUid(uid);

        List<DeskInfo> deskInfos = deskInfoMapper.getAllByOrderUid(uid);

        //将材料信息置为删除状态
        if (CollectionUtil.isNotEmpty(materialInfos)) {
            materialInfos.stream().forEach(materialInfo -> {
                materialInfo.setStatus(SysConf.DELETE_STATUS);
            });

            materialInfoService.updateBatchById(materialInfos);
        }

        /**
         * 将五金信息置为删除状态
         */
        if (CollectionUtil.isNotEmpty(ironwareInfos)) {
            ironwareInfos.stream().forEach(ironwareInfo -> {
                ironwareInfo.setStatus(SysConf.DELETE_STATUS);
            });

            ironwareInforService.updateBatchById(ironwareInfos);
        }

        //将材料信息置为删除状态
        if (CollectionUtil.isNotEmpty(metalInfos)) {
            metalInfos.stream().forEach(metalInfo -> {
                metalInfo.setStatus(SysConf.DELETE_STATUS);
            });

            metalInfoService.updateBatchById(metalInfos);
        }

        if(CollectionUtil.isNotEmpty(deskInfos)){
            deskInfos.stream().forEach(deskInfo -> {
                deskInfo.setStatus(SysConf.DELETE_STATUS);
            });

            deskInfoService.updateBatchById(deskInfos);
        }

        QueryWrapper<LaminateInfo> laminateInfoQueryWrapper = new QueryWrapper<>();
        laminateInfoQueryWrapper.eq("order_info_uid", uid);

        List<LaminateInfo> laminateInfos = laminaterInfoMapper.selectList(laminateInfoQueryWrapper);

        if (CollectionUtil.isNotEmpty(laminateInfos)) {
            laminateInfos.stream().forEach(laminateInfo -> {
                laminateInfo.setStatus(SysConf.DELETE_STATUS);
            });

            laminateInfoService.updateBatchById(laminateInfos);
        }

        //将玻璃置为删除状态
        QueryWrapper<OrderGlassDetail> detailQueryWrapper = new QueryWrapper<>();
        detailQueryWrapper.eq("order_uid",uid);
        List<OrderGlassDetail> orderGlassDetails = orderGlassDetailService.list(detailQueryWrapper);

        if (CollectionUtil.isNotEmpty(orderGlassDetails)) {
            orderGlassDetails.stream().forEach(orderGlassDetail -> {
                orderGlassDetail.setStatus(SysConf.DELETE_STATUS);
            });
            orderGlassDetailService.updateBatchById(orderGlassDetails);
        }


    }

    private void getOrderGlassDetail(List<LaminateInfo> laminateInfos, OrderInfo orderInfo, OrderGlassDetailMapper orderGlassDetailMapper) throws Exception{


        for (LaminateInfo laminateInfo : laminateInfos) {
            if (laminateInfo.getMaterialType().code != MaterialTypeEnums.CBDJJ.code) {

                ProductCalculateEnums enums = ProductCalculateEnums.getByCode(laminateInfo.getMaterialType().code);
                String glassDetail = enums.getGlassType() + laminateInfo.getGlassColor().name ;

                OrderGlassDetail orderGlassDetail = new OrderGlassDetail();
                orderGlassDetail.setOrderId(orderInfo.getOrderId());
                orderGlassDetail.setMaterialUid(laminateInfo.getUid());
                orderGlassDetail.setOrderUid(laminateInfo.getOrderInfouId());
                orderGlassDetail.setGlassColor(laminateInfo.getGlassColor().name);
                orderGlassDetail.setGlassHeight(laminateInfo.getGlassWidth());
                orderGlassDetail.setGlassWidth(laminateInfo.getGlassDepth());
                orderGlassDetail.setMaterialNum(laminateInfo.getLaminateNum());
                orderGlassDetail.setMaterialType(laminateInfo.getMaterialType().name);
                orderGlassDetail.setGlassDetail(glassDetail);
                orderGlassDetail.setCustomerName(orderInfo.getCustomerName());

               orderGlassDetailMapper.insert(orderGlassDetail);
               try {
                   Thread.sleep(1000);
               }catch (Exception e){
                   throw new MessageException("线程错误");
               }
            }
        }

    }

    private void getOrderGlassDetailByMaterial(List<MaterialInfo> materialInfos, OrderInfo orderInfo,OrderGlassDetailMapper orderGlassDetailMapper) throws Exception{

        for (MaterialInfo materialInfo : materialInfos) {

            ProductCalculateEnums enums = ProductCalculateEnums.getByCode(materialInfo.getMaterialType().code);
            String glassDetail = enums.getGlassType() + materialInfo.getGlassColor().name;

            OrderGlassDetail orderGlassDetail = new OrderGlassDetail();
            orderGlassDetail.setOrderId(orderInfo.getOrderId());
            orderGlassDetail.setMaterialUid(materialInfo.getUid());
            orderGlassDetail.setOrderUid(materialInfo.getOrderInfouId());
            orderGlassDetail.setGlassColor(materialInfo.getGlassColor().name);
            orderGlassDetail.setGlassHeight(materialInfo.getGlassHeight());
            orderGlassDetail.setGlassWidth(materialInfo.getGlassWidth());
            orderGlassDetail.setMaterialNum(materialInfo.getMaterialNum());
            orderGlassDetail.setMaterialType(materialInfo.getMaterialType().name);
            orderGlassDetail.setGlassDetail(glassDetail);
            orderGlassDetail.setCustomerName(orderInfo.getCustomerName());

            orderGlassDetailMapper.insert(orderGlassDetail);
            try {
                Thread.sleep(1000);
            }catch (Exception e){
                throw new MessageException("线程错误！");
            }


        }
    }
}














