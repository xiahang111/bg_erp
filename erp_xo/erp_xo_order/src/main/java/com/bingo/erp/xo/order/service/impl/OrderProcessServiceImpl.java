package com.bingo.erp.xo.order.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.erp.base.enums.OrderStatusEnums;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.commons.entity.OrderProcess;
import com.bingo.erp.commons.entity.OrderProcessAnalyze;
import com.bingo.erp.utils.DateUtils;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.order.global.ExcelConf;
import com.bingo.erp.xo.order.global.SysConf;
import com.bingo.erp.xo.order.mapper.OrderProcessMapper;
import com.bingo.erp.xo.order.service.OrderProcessAnalyzeService;
import com.bingo.erp.xo.order.service.OrderProcessService;
import com.bingo.erp.xo.order.tools.AnalyzeTools;
import com.bingo.erp.xo.order.tools.OrderTools;
import com.bingo.erp.xo.order.vo.OrderProcessAnalyzePageVO;
import com.bingo.erp.xo.order.vo.OrderProcessPageVO;
import com.bingo.erp.xo.order.vo.OrderProcessVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;

@Service
public class OrderProcessServiceImpl extends SuperServiceImpl<OrderProcessMapper, OrderProcess> implements OrderProcessService {

    @Value(value = "${srcFileUrl}")
    private String SRC_FILE_URL;


    @Value(value = "${processFileUrl}")
    private String PROCESS_FILE_URL;

    @Resource
    private AnalyzeTools analyzeTools;

    @Resource
    private OrderProcessService orderProcessService;

    @Resource
    private OrderProcessMapper orderProcessMapper;

    @Resource
    private OrderProcessAnalyzeService orderProcessAnalyzeService;

    @Resource
    private OrderTools orderTools;

    @Override
    public IPage<OrderProcess> getProcessRecord(OrderProcessPageVO orderProcessPageVO) {

        QueryWrapper<OrderProcess> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("status", SysConf.NORMAL_STATUS);

        if (StringUtils.isNotBlank(orderProcessPageVO.getKeyword())) {
            queryWrapper.like("customer_name", (orderProcessPageVO.getKeyword())).
                    or().like("express", (orderProcessPageVO.getKeyword())).
                    or().like("express_real", (orderProcessPageVO.getKeyword())).
                    or().like("customer_addr", (orderProcessPageVO.getKeyword())).
                    or().like("remark", (orderProcessPageVO.getKeyword()));

        }

        if (StringUtils.isNotBlank(orderProcessPageVO.getOrderStatus())) {
            queryWrapper.eq("order_status", orderProcessPageVO.getOrderStatus());
        }

        if (StringUtils.isNotBlank(orderProcessPageVO.getOrderType())) {
            queryWrapper.eq("order_type", orderProcessPageVO.getOrderType());
        }

        if (null != orderProcessPageVO.getDateScope() && orderProcessPageVO.getDateScope().size() > 0) {
            List<Date> dateList = orderProcessPageVO.getDateScope();
            queryWrapper.between("order_date", dateList.get(0), dateList.get(1));
        }

        Page<OrderProcess> processPage = new Page<>();
        processPage.setCurrent(orderProcessPageVO.getCurrentPage());
        processPage.setSize(orderProcessPageVO.getPageSize());

        IPage<OrderProcess> iPage = orderProcessService.page(processPage, queryWrapper);

        return iPage;
    }

    @Override
    public IPage<OrderProcessAnalyze> getProcessRecordAnalyze(OrderProcessAnalyzePageVO orderProcessAnalyzePageVO) {

        QueryWrapper<OrderProcessAnalyze> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");

        Page<OrderProcessAnalyze> processAnalyzePage = new Page<>();
        processAnalyzePage.setCurrent(orderProcessAnalyzePageVO.getCurrentPage());
        processAnalyzePage.setSize(orderProcessAnalyzePageVO.getPageSize());

        IPage<OrderProcessAnalyze> page = orderProcessAnalyzeService.page(processAnalyzePage, queryWrapper);
        List<OrderProcessAnalyze> list = page.getRecords();
        list.stream().forEach(processAnalyze -> {
            String[] names = processAnalyze.getFileName().split("\\\\");
            processAnalyze.setFileName(names[names.length-1]);
        });
        page.setRecords(list);
        return page;
    }

   /* public static void main(String[] args) {
        String aaa = "D:\\workspace\\bg_erp\\excel\\process\\生产流程表20210311891779.xls";
        String[] names = aaa.split("\\\\");
        System.out.println(names[names.length-1]);

    }*/

    @Override
    public void deleteByUid(String uid) throws Exception {
        if (StringUtils.isBlank(uid)) {
            throw new MessageException("传入uid为空！");
        }

        QueryWrapper<OrderProcess> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("uid", uid);

        OrderProcess queryOne = orderProcessMapper.selectOne(queryWrapper);

        if (null == queryOne) {
            throw new MessageException("库内没有此信息");
        }

        queryOne.setStatus(SysConf.DELETE_STATUS);

        orderProcessMapper.updateById(queryOne);
    }

    @Override
    public void deleteByOrderUid(String orderUid) throws Exception {

        if (StringUtils.isBlank(orderUid)) {
            throw new MessageException("传入uid为空！");
        }

        QueryWrapper<OrderProcess> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", SysConf.NORMAL_STATUS);
        queryWrapper.eq("order_info_uid", orderUid);

        OrderProcess queryOne = orderProcessMapper.selectOne(queryWrapper);

        if (null == queryOne) {
            return;
        }

        queryOne.setStatus(SysConf.DELETE_STATUS);

        orderProcessMapper.updateById(queryOne);

    }

    @Override
    public void updateProcessRecord(OrderProcessVO orderProcessVO) throws Exception {

        QueryWrapper<OrderProcess> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", orderProcessVO.getUid());
        OrderProcess orderProcess = orderProcessService.getOne(queryWrapper);

        if (null == orderProcess) {
            throw new MessageException("数据库中无此订单记录");
        }

        if (StringUtils.isNotBlank(orderProcessVO.getOrderStatus())) {
            orderProcess.setOrderStatus(OrderStatusEnums.getByCode(Integer.valueOf(orderProcessVO.getOrderStatus())));
        }

        if (null != orderProcessVO.getDeliveryDate()) {
            orderProcess.setDeliveryDate(orderProcessVO.getDeliveryDate());
        }

        if (StringUtils.isNotBlank(orderProcessVO.getRestPrice())) {
            orderProcess.setRestPrice(orderProcessVO.getRestPrice());
        }

        if (StringUtils.isNotBlank(orderProcessVO.getMaterialNum())) {
            orderProcess.setMaterialNum(orderProcessVO.getMaterialNum());
        }

        if (null != orderProcessVO.getGlassHave()) {
            orderProcess.setGlassHave(orderProcessVO.getGlassHave());
        }

        if (null != orderProcessVO.getGlassArrive()) {
            orderProcess.setGlassArrive(orderProcessVO.getGlassArrive());
        }

        if (null != orderProcessVO.getMaterialDate()) {
            orderProcess.setMaterialDate(orderProcessVO.getMaterialDate());
        }

        if (null != orderProcessVO.getDigitalDate()) {
            orderProcess.setDigitalDate(orderProcessVO.getDigitalDate());
        }

        if (null != orderProcessVO.getDrillDate()) {
            orderProcess.setDrillDate(orderProcessVO.getDrillDate());
        }

        if (null != orderProcessVO.getAssembleDate()) {
            orderProcess.setAssembleDate(orderProcessVO.getAssembleDate());
        }

        if (null != orderProcessVO.getPackageDate()) {
            orderProcess.setPackageDate(orderProcessVO.getPackageDate());
        }

        if (StringUtils.isNotBlank(orderProcessVO.getPackageNum())) {
            orderProcess.setPackageNum(orderProcessVO.getPackageNum());
        }

        if (StringUtils.isNotBlank(orderProcessVO.getRemark())) {
            orderProcess.setRemark(orderProcessVO.getRemark());
        }

        if (StringUtils.isNotBlank(orderProcessVO.getCustomerAddr())) {
            orderProcess.setCustomerAddr(orderProcessVO.getCustomerAddr());
        }

        if (StringUtils.isNotBlank(orderProcessVO.getExpressReal())) {
            orderProcess.setExpressReal(orderProcessVO.getExpressReal());
        }

        if (StringUtils.isNotBlank(orderProcessVO.getExpressId())) {
            orderProcess.setExpressId(orderProcessVO.getExpressId());
        }

        if (null != orderProcessVO.getDeliveryDateReal()) {
            orderProcess.setDeliveryDateReal(orderProcessVO.getDeliveryDateReal());
        }

        orderProcessMapper.updateById(orderProcess);
    }

    @Override
    public void saveOrderProcessByOrderInfo(OrderInfo orderInfo) throws Exception {

        if (StringUtils.isBlank(orderInfo.getUid())) {
            throw new MessageException("没有订单uid!");
        }

        OrderProcess orderProcess = new OrderProcess();

        orderProcess.setOrderInfouid(orderInfo.getUid());
        if (StringUtils.isBlank(orderInfo.getOrderId())) {
            throw new MessageException("没有订单id!");
        }
        orderProcess.setOrderId(orderInfo.getOrderId());
        orderProcess.setOrderStatus(orderInfo.getOrderStatus());
        orderProcess.setOrderDate(null == orderInfo.getOrderDate() ? new Date() : orderInfo.getOrderDate());
        orderProcess.setDeliveryDate(orderInfo.getDeliveryDate() == null ? DateUtils.getDate(DateUtils.getNowTime(), 10) : (orderInfo.getDeliveryDate()));

        orderProcess.setOrderType(orderInfo.getOrderType().getName());
        if ((orderInfo.getOrderType().code == 1 || orderInfo.getOrderType().code == 2) && orderInfo.getProductType().code == 1) {
            orderProcess.setGlassHave(true);
        } else {
            orderProcess.setGlassHave(false);
        }

        orderProcess.setGlassArrive(false);

        orderProcess.setPackageNum(orderInfo.getBigPackageNum() + "(大包装) " + orderInfo.getSimplePackageNum() + "(小包装) ");
        orderProcess.setCustomerName(orderInfo.getCustomerName());
        orderProcess.setCustomerPhone(orderInfo.getCustomerPhoneNum());
        orderProcess.setCustomerAddr(orderInfo.getCustomerAddr());
        orderProcess.setExpress(orderInfo.getExpress());

        orderProcessMapper.insert(orderProcess);


    }

    @Override
    @Transactional
    public void fileAnalyze(MultipartFile file) throws Exception {
        String suffix = DateUtils.formateDate(new Date(), DateUtils.DAYFORMAT_STRING) + RandomUtil.randomNumbers(6) + ExcelConf.FILE_SUFFIX;

        String fileName = "生产流程表" + suffix;

        File saveFile = new File(SRC_FILE_URL + PROCESS_FILE_URL + fileName);
        file.transferTo(saveFile);

        OrderProcessAnalyze orderProcessAnalyze = new OrderProcessAnalyze();
        orderProcessAnalyze.setFileName(SRC_FILE_URL + PROCESS_FILE_URL + fileName);

        orderProcessAnalyzeService.save(orderProcessAnalyze);
        //发一条消息
        orderTools.processAnalyze(orderProcessAnalyze);
    }


}






























