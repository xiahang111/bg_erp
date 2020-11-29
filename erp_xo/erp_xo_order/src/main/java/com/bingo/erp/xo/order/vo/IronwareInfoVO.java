package com.bingo.erp.xo.order.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IronwareInfoVO {

    public String ironwareName = "";

    public String unit = "";

    public int ironwareColor;

    public String specification = "";

    public int ironwareNum;

    public BigDecimal price = new BigDecimal("0");

    private String remark = "";

    protected BigDecimal totalPrice = new BigDecimal("0");

}
