package com.bingo.erp.xo.wx.dto;

import lombok.Data;

@Data
public class OrderDTO {

    private String orderId;

    private String customerName;

    private String orderMaker;

    private String totalPrice;

    private String orderDate;

    private String phoneNum;

    private String customerAddr;
}
