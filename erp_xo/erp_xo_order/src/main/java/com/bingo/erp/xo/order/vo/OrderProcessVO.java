package com.bingo.erp.xo.order.vo;

import lombok.Data;

import java.util.Date;

@Data
public class OrderProcessVO {

    private String uid;

    private String orderInfouid;

    private String orderStatus;

    private Date deliveryDate;

    private String restPrice;

    private String orderType;

    private String materialNum;

    private Boolean glassHave;

    private Boolean glassArrive;

    private Date materialDate;

    private Date digitalDate;

    private Date drillDate;

    private Date assembleDate;

    private Date packageDate;

    private String packageNum;

    private String remark;

    private String customerName;

    private String customerPhone;

    private String customerAddr;

    private String express;

    private String expressId;

    private String expressReal;

    private Date deliveryDateReal;
}

