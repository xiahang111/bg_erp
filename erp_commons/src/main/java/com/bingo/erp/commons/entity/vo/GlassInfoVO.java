package com.bingo.erp.commons.entity.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class GlassInfoVO {

    private String orderId;

    private BigDecimal glassHeight;

    private BigDecimal glassWidth;

    private String MaterialType;

    private String glassDetail;

    private Integer glassNum;

    private Date createTime;

    public GlassInfoVO() {
    }

    public GlassInfoVO(String orderId, BigDecimal glassHeight, BigDecimal glassWidth, String MaterialType, Integer glassNum, Date createTime,String glassDetail) {
        this.orderId = orderId;
        this.glassHeight = glassHeight;
        this.glassWidth = glassWidth;
        this.MaterialType = MaterialType;
        this.glassNum = glassNum;
        this.createTime = createTime;
        this.glassDetail = glassDetail;
    }
}
