package com.bingo.erp.xo.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductVO {

    /**
     * 是否是净尺寸
     */
    public Boolean isClear;
    /**
     * 产品类型 成品半成品
     */
    public int productType;

    public int orderType;

    public int orderStatus;

    /**
     * 收货人
     */
    public String customerName;

    /**
     * 客户昵称
     */
    public String customerNick;

    /**
     * 收货地址
     */
    public String customerAddr;

    /**
     * 客户手机号
     */
    public String customerPhoneNum;


    /**
     * 快递信息
     */
    public String express;

    /**
     * 业务员
     */
    public String salesman;

    /**
     * 制单人
     */
    public String orderMaker;

    /**
     * 订单总金额
     */
    public BigDecimal orderTotalPrice;

    /**
     * 总订单备注
     */
    public String remark;

    /**
     * 订单编号
     */
    public String orderId;


    public int bigPackageNum;

    public int simplePackageNum;

    public Date orderDate;

    public Date deliveryDate;
}
