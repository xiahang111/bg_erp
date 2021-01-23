package com.bingo.erp.xo.order.dto;

import lombok.Data;

@Data
public class OrderDTO {

    private String orderId = "";

    private String customerName = "暂无";

    private String orderMaker = "暂无";

    private String totalPrice = "未知";

    private String orderDate = "未知";

    private String phoneNum = "未知";

    private String customerAddr = "/";
}
