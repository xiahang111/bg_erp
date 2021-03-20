package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import lombok.Data;

@Data
@TableName("t_order_express")
public class OrderExpress extends SuperEntity<OrderExpress> {

    //订单id
    private String orderId;

    private String orderMaker;

    //物流公司名称
    private String express;

    //物流单号
    private String expressId;

    //物流到达地址
    private String expressAddr;
}
