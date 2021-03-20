package com.bingo.erp.xo.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.Admin;
import com.bingo.erp.commons.entity.ExpressRecord;
import com.bingo.erp.commons.entity.OrderExpress;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.order.mapper.OrderExpressMapper;
import com.bingo.erp.xo.order.service.AdminService;
import com.bingo.erp.xo.order.service.ExpressRecordService;
import com.bingo.erp.xo.order.service.OrderExpressService;
import com.bingo.erp.xo.order.vo.OrderExpressPageVO;
import com.bingo.erp.xo.order.vo.OrderExpressVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class OrderExpressServiceImpl extends SuperServiceImpl<OrderExpressMapper, OrderExpress> implements OrderExpressService {

    @Resource
    private OrderExpressService orderExpressService;

    @Resource
    private AdminService adminService;

    @Resource
    private ExpressRecordService expressRecordService;

    @Override
    public IPage<OrderExpress> getOrderExpressPage(OrderExpressPageVO orderExpressPageVO) {

        QueryWrapper<OrderExpress> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(orderExpressPageVO.getOrderId())) {
            queryWrapper.eq("order_id", orderExpressPageVO.getOrderId());
        }
        if (StringUtils.isNotBlank(orderExpressPageVO.getExpressId())) {
            queryWrapper.eq("express_id", orderExpressPageVO.getOrderId());
        }

        if (StringUtils.isNotBlank(orderExpressPageVO.getExpress())) {
            queryWrapper.like("express", orderExpressPageVO.getExpress());

        }
        if (StringUtils.isNotBlank(orderExpressPageVO.getExpressAddr())) {
            queryWrapper.like("express_addr", orderExpressPageVO.getExpressAddr());
        }

        if (null != orderExpressPageVO.getDateScope() && orderExpressPageVO.getDateScope().size() > 0) {
            List<Date> dateList = orderExpressPageVO.getDateScope();
            queryWrapper.between("create_time", dateList.get(0), dateList.get(1));
        }


        Page<OrderExpress> processPage = new Page<>();
        processPage.setCurrent(orderExpressPageVO.getCurrentPage());
        processPage.setSize(orderExpressPageVO.getPageSize());

        IPage<OrderExpress> iPage = orderExpressService.page(processPage, queryWrapper);

        return iPage;
    }


    @Override
    public void saveOrUpdate(String adminUid, OrderExpressVO orderExpressVO) throws Exception {

        Admin admin = adminService.getById(adminUid);

        QueryWrapper<OrderExpress> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(orderExpressVO.getExpress())){
            saveExpressRecord(orderExpressVO.getExpress());
        }

        if (StringUtils.isNotBlank(orderExpressVO.getUid())) {
            //更新方法

            queryWrapper.eq("order_id", orderExpressVO.getOrderId());
            OrderExpress orderExpress = orderExpressService.getOne(queryWrapper);

            if (null == orderExpress) {
                throw new MessageException("更新失败!数据库内无此物流记录!!!");
            }

            if (StringUtils.isNotBlank(orderExpressVO.getExpress())) {
                orderExpress.setExpress(orderExpressVO.getExpress());
            }

            if (StringUtils.isNotBlank(orderExpressVO.getExpressId())) {
                orderExpress.setExpressId(orderExpressVO.getExpressId());
            }

            orderExpress.setOrderMaker(admin.getNickName());

            orderExpressService.updateById(orderExpress);

        } else {
            //增加方法

            if (StringUtils.isBlank(orderExpressVO.getOrderId()) ||
                    StringUtils.isBlank(orderExpressVO.getExpress()) ||
                    StringUtils.isBlank(orderExpressVO.getExpressId())) {
                throw new MessageException("信息不完全!!!请确认!");
            }

            OrderExpress orderExpress = new OrderExpress();
            orderExpress.setExpress(orderExpressVO.getExpress());
            orderExpress.setExpressId(orderExpressVO.getExpressId());
            orderExpress.setOrderId(orderExpressVO.getOrderId());
            orderExpress.setOrderMaker(admin.getNickName());
            orderExpressService.save(orderExpress);
        }
    }


    private void saveExpressRecord(String tempString) {


        StringBuffer stringBuffer = new StringBuffer();
        //把这一行中的中文字符替换成英文字符
        for (int i = 0; i < tempString.length(); i++) {
            //把这个字符串中的中文括号换成英文括号
            if (tempString.charAt(i) == '（')
                stringBuffer.append('(');
            else if (tempString.charAt(i) == '）')
                stringBuffer.append(')');
            else if (tempString.charAt(i) == '，')
                stringBuffer.append(',');
            else if (tempString.charAt(i) == '＠')
                stringBuffer.append('@');
            else if (tempString.charAt(i) == '；')
                stringBuffer.append(';');
            else
                stringBuffer.append(tempString.charAt(i));
        }

        String expressName = stringBuffer.toString();

        QueryWrapper<ExpressRecord> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("express_name",expressName);

        ExpressRecord expressRecord = expressRecordService.getOne(queryWrapper);

        if (expressRecord == null){
            expressRecord = new ExpressRecord();
            expressRecord.setExpressName(expressName);

            expressRecordService.save(expressRecord);
        }


    }
}
