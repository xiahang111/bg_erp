package com.bingo.erp.xo.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 坯料信息
 */
@Data
public class StoreOriginVO {

    private String uid;

    private String materialName;

    private BigDecimal materialNum;

    private String specification;

    private String unit;

    private BigDecimal price;

    private BigDecimal weight;
}
