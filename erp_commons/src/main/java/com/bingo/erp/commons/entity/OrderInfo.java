package com.bingo.erp.commons.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import com.bingo.erp.base.enums.OrderStatusEnums;
import com.bingo.erp.base.enums.OrderTypeEnums;
import com.bingo.erp.base.enums.ProductTypeEnums;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("t_order_info")
@Data
public class OrderInfo extends SuperEntity<OrderInfo> {

    /**
     * 是否是净尺寸
     */
    private Boolean isClear = true;

    @TableField("admin_uid")
    String adminUid;


    /**
     * 产品类型（成品、半成品）
     */
    private ProductTypeEnums productType;

    /**
     * 订单类型
     */
    private OrderTypeEnums orderType;

    private OrderStatusEnums orderStatus;

    /**
     * 用户相关信息
     */
    private String customerNick;

    private String customerName;

    private String customerAddr;

    private String customerPhoneNum;


    /**
     * 大包装个数
     */
    private Integer bigPackageNum;

    /**
     * 小包装个数
     */
    private Integer simplePackageNum;

    /**
     * 下单日期
     */
    private Date orderDate;

    /**
     * 交付日期
     */
    private Date deliveryDate;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 业务员
     */
    private String salesman;

    /**
     * 制单员
     */
    private String orderMaker;


    /**
     * 订单总金额
     */
    private BigDecimal totalPrice;


    /**
     * 快递信息
     */
    private String express;

    public OrderInfo() {
    }

    public OrderInfo(Boolean isClear,
                     ProductTypeEnums productType,
                     String customerNick,
                     String customerName,
                     String customerAddr,
                     String customerPhoneNum,
                     Integer bigPackageNum,
                     Integer simplePackageNum,
                     Date orderDate,
                     Date deliveryDate,
                     String orderId,
                     String salesman,
                     String orderMaker,
                     BigDecimal totalPrice,
                     String express) {
        this.isClear = isClear;
        this.productType = productType;
        this.customerNick = customerNick;
        this.customerName = customerName;
        this.customerAddr = customerAddr;
        this.customerPhoneNum = customerPhoneNum;
        this.bigPackageNum = bigPackageNum;
        this.simplePackageNum = simplePackageNum;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.orderId = orderId;
        this.salesman = salesman;
        this.orderMaker = orderMaker;
        this.totalPrice = totalPrice;
        this.express = express;
    }
}
