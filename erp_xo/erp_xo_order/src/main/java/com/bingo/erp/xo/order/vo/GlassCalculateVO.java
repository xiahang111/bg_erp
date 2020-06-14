package com.bingo.erp.xo.order.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GlassCalculateVO {

    /**
     * 产品Id
     */
    private String  productId;


    /**
     * 宽度
     */
    private BigDecimal width;


    /**
     * 长度
     */
    private BigDecimal height;


    /**
     * 是否是批量导入
     */
    private Boolean isBatch;


    /**
     * 批量导入数据
     */
    private String batchData;

}
