package com.bingo.erp.xo.order.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatementResultVO {

    private String createTime ;

    private Integer materialStatus;

    private String materialName;

    private Integer materialColor;

    private BigDecimal totalPrice;

    private BigDecimal totalNum;
}
