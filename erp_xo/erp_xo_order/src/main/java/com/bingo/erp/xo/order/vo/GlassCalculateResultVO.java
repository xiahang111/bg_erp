package com.bingo.erp.xo.order.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GlassCalculateResultVO {

    /**
     * 产品id
     */
    private String  productUid;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 高度计算结果
     */
    private BigDecimal heightResult;

    /**
     * 宽度计算结果
     */
    private BigDecimal widthResult;

    public GlassCalculateResultVO(String productUid) {
        this.productUid = productUid;
    }
}
