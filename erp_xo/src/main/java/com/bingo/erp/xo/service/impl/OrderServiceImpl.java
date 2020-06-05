package com.bingo.erp.xo.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.db.sql.Order;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.erp.base.enums.*;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.fatocry.material.MaterialCalculateFactory;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.*;
import com.bingo.erp.utils.DateUtils;
import com.bingo.erp.xo.enums.MaterialFactoryEnum;
import com.bingo.erp.xo.global.ExcelConf;
import com.bingo.erp.xo.global.NormalConf;
import com.bingo.erp.xo.mapper.*;
import com.bingo.erp.xo.service.OrderService;
import com.bingo.erp.xo.tools.OrderTools;
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
    private TransomMapper transomMapper;

    @Resource
    private MaterialInfoMapper materialInfoMapper;

    @Resource
    private IronwareInfoMapper ironwareInfoMapper;

    @Resource
    private OrderService orderService;

    @Resource
    OrderTools tools;


    @Override
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
    public List<String> saveOrder(MaterialVO materialVO) throws Exception {

        log.info("===============方法开始，参数信息：excel文件夹：" + ExcelConf.NEW_FILE_DICT);

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

        int materialNum = materialVO.getMaterials().size() - 1;

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
                orderFileName = ExcelConf.SRC_FILE_URL + ExcelConf.TDHL_ORDER_FILENAME;
                productSrcFileName = ExcelConf.SRC_FILE_URL + ExcelConf.TDHL_PRODUCT_ORDER_FILENAME;
            } else {
                orderFileName = ExcelConf.SRC_FILE_URL + ExcelConf.ORDER_FILENAME;
                productSrcFileName = ExcelConf.SRC_FILE_URL + ExcelConf.PRODUCT_ORDER_FILENAME;

            }

            POIFSFileSystem fs = new POIFSFileSystem(new File(orderFileName));

            HSSFWorkbook wb = new HSSFWorkbook(fs);

            HSSFSheet sheet = wb.getSheetAt(0);

            Map<String, List<MaterialInfoVO>> map = tools.materialsToMap(materialVO.getMaterials());

            int addnum = tools.extensionExcel(sheet, map, ironwareNum, transomNum);

            //填充料玻、五金数据
            tools.fillData(sheet, map, materialVO, materialVO.getIronwares(), addnum);

            File newFile = new File(ExcelConf.NEW_FILE_DICT + fileName);

            wb.write(newFile);

            Map<String, Object> dataMap = tools.toMap(materialVO);

            XLSTransformer transformer = new XLSTransformer();
            transformer.transformXLS(ExcelConf.NEW_FILE_DICT + fileName, dataMap, ExcelConf.NEW_FILE_DICT + fileName);

            /**
             =========================== 生产单制作 ================================
             */

            String productFileName = "生产单" + suffix;

            File productFile = new File(ExcelConf.NEW_FILE_DICT + productFileName);

            POIFSFileSystem productFs = new POIFSFileSystem(new File(productSrcFileName));

            HSSFWorkbook productWb = new HSSFWorkbook(productFs);

            HSSFSheet productSheet = productWb.getSheetAt(0);

            int addnum1 = tools.productExtensionExcel(productSheet, map, materialVO, materialNum);

            tools.productFillData(productSheet, addnum1, map, materialVO, materialVO.getIronwares());

            productWb.write(productFile);

            Thread.sleep(1000l);

            transformer.transformXLS(ExcelConf.NEW_FILE_DICT + productFileName, dataMap, ExcelConf.NEW_FILE_DICT + productFileName);

            result.add(productFileName);

            /**
             * ==========================================存入数据库 ======================================
             */

            OrderInfo orderInfo = tools.getOrderInfo(materialVO);

            orderInfoMapper.insert(orderInfo);

            tools.saveMaterialInfoList(materialInfoMapper, orderInfo.getUid(), materialVO.getMaterials());

            tools.saveIronwareInfoList(ironwareInfoMapper, materialVO.getIronwares(), orderInfo.getUid());

            if (materialVO.isHaveTransom && null != materialVO.getTransoms() && materialVO.getTransoms().size() > 0) {

                tools.saveTransomInfoList(transomMapper, materialVO.transoms, orderInfo.getUid());
            }

            log.info("主键id" + orderInfo.getUid());

            return result;

        } catch (Exception e) {

            e.printStackTrace();
            throw new MessageException("生成表格失败！");
        }


    }

    @Override
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
    public MaterialVO getMaterialVOByUid(String uid) {

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("uid", uid);

        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);

        List<MaterialInfo> materialInfos = materialInfoMapper.getAllByOrderUid(uid);

        List<IronwareInfo> ironwareInfos = ironwareInfoMapper.getAllByOrderUid(uid);

        MaterialVO materialVO = tools.revertToMaterialVO(orderInfo, materialInfos, ironwareInfos);


        return materialVO;
    }
}














