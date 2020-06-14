package com.bingo.erp.xo.order.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IndexOrderVO {

    private String orderId;

    private BigDecimal totalPrice;

    private String salesman;

    private int status;

    public IndexOrderVO() {
    }

    public IndexOrderVO(String orderId, BigDecimal totalPrice, String salesman, int status) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.salesman = salesman;
        this.status = status;
    }
}
