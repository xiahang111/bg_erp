package com.bingo.erp.xo.order.dto;

import lombok.Data;

@Data
public class DvOrderNumDTO {

    //总订单数
    private String totalOrderNum;

    //当天订单数
    private String todayOrderNum;

    //未完成订单数
    private String completeOrderNum;

    //已经完成订单数
    private String WorkingOrderNum;
}
