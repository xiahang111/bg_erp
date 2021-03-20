package com.bingo.erp.xo.order.vo;

import com.bingo.erp.base.vo.PageInfo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderProcessPageVO extends PageInfo<OrderProcessPageVO> {

    private String orderStatus;

    private String orderType;

    private List<Date> dateScope;
}
