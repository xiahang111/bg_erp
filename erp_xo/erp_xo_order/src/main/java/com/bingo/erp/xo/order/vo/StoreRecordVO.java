package com.bingo.erp.xo.order.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreRecordVO {

    private Integer materialResource;

    private String materialName;

    private Integer materialColor;

    private BigDecimal materialNum;

    private String specification;

    private String unit;

    private Integer materialStatus;
}
