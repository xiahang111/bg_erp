package com.bingo.erp.xo.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreSummaryVO {

    private String uid;

    private String materialName;

    private BigDecimal materialNum;

    private Integer materialColor;

    private String specification;

    private String unit;

    private BigDecimal price;

    private BigDecimal weight;
}
