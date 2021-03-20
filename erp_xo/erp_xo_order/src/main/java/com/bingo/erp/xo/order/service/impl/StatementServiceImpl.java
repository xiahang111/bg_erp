package com.bingo.erp.xo.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bingo.erp.base.enums.OrderTypeEnums;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.order.dto.SaleStatementDTO;
import com.bingo.erp.xo.order.global.SysConf;
import com.bingo.erp.xo.order.service.OrderService;
import com.bingo.erp.xo.order.service.StatementService;
import com.bingo.erp.xo.order.vo.SaleStatementVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class StatementServiceImpl implements StatementService {

    @Autowired
    private OrderService orderService;

    @Override
    public SaleStatementDTO getSaleStatement(SaleStatementVO saleStatementVO) throws MessageException{

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();


        if (StringUtils.isNotBlank(saleStatementVO.getSalemen())){
            queryWrapper.like("salesman",saleStatementVO.getSalemen());
        }
        //**查询正常订单
        queryWrapper.eq("status",SysConf.NORMAL_STATUS);
        queryWrapper.orderByDesc("create_time");

        if (StringUtils.isNotBlank(saleStatementVO.getProductType())){
            queryWrapper.eq("product_type",saleStatementVO.getProductType());
        }

        if (StringUtils.isNotBlank(saleStatementVO.getOrderType())){
            queryWrapper.eq("order_type",saleStatementVO.getOrderType());
        }

        if (null != saleStatementVO.getDateScope() && saleStatementVO.getDateScope().size() > 0){
            List<Date> dateList = saleStatementVO.getDateScope();
            queryWrapper.between("create_time",dateList.get(0),dateList.get(1));
        }

        if(StringUtils.isNotBlank(saleStatementVO.getOrdermaker())){
            queryWrapper.like("order_maker",saleStatementVO.getOrdermaker());
        }

        List<OrderInfo> orderInfos = orderService.list(queryWrapper);

        try {
            SaleStatementDTO saleStatementDTO = analyzeOrderInfosToResult(orderInfos);
            return saleStatementDTO;
        }catch (Exception e){
            e.printStackTrace();
            throw new MessageException(e.getMessage());
        }

    }

    private SaleStatementDTO analyzeOrderInfosToResult(List<OrderInfo> orderInfos){

        SaleStatementDTO saleStatementDTO = new SaleStatementDTO();

        saleStatementDTO.setOrderInfos(orderInfos);
        if(null == orderInfos || orderInfos.size() <= 0){
            return saleStatementDTO;
        }

        for (OrderInfo orderInfo:orderInfos) {

            saleStatementDTO.setOrderNum(saleStatementDTO.getOrderNum()+1);
            saleStatementDTO.setPriceNum(saleStatementDTO.getPriceNum().add(orderInfo.getTotalPrice()).setScale(2));

            if (orderInfo.getOrderType().code == OrderTypeEnums.DOORORDER.code){
                saleStatementDTO.setDoorOrderNum(saleStatementDTO.getDoorOrderNum()+1);
                saleStatementDTO.setDoorPriceNum(saleStatementDTO.getDoorPriceNum().add(orderInfo.getTotalPrice()).setScale(2));
            }

            if (orderInfo.getOrderType().code == OrderTypeEnums.CBDORDER.code){
                saleStatementDTO.setLaminateOrderNum(saleStatementDTO.getLaminateOrderNum()+1);
                saleStatementDTO.setLaminatePriceNum(saleStatementDTO.getLaminatePriceNum().add(orderInfo.getTotalPrice()).setScale(2));
            }
        }

        return saleStatementDTO;
    }
}
