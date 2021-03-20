package com.bingo.erp.xo.order.vo;

import com.bingo.erp.base.vo.PageInfo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderExpressPageVO extends PageInfo<OrderExpressPageVO> {

    private String orderId;

    private String expressId;

    private String express;

    private String expressAddr;

    private List<Date> dateScope;
}
