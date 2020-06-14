package com.bingo.erp.xo.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 天地横梁信息VO
 */
@Data
public class TransomVO {

    private int transomType;

    private int transomColor;

    private BigDecimal height;

    private int transomNum;

    private BigDecimal price;

    private BigDecimal totalPrice;

    private String remark;
}
