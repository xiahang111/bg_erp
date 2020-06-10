package com.bingo.erp.commons.entity.vo;

import com.bingo.erp.base.enums.GlassColor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class GlassInfoVO {

    private String orderId;

    private BigDecimal glassHeight;

    private BigDecimal glassWidth;

    private String glassColor;

    private Integer glassNum;

    private Date createTime;

    public GlassInfoVO() {
    }

    public GlassInfoVO(String orderId, BigDecimal glassHeight, BigDecimal glassWidth, String glassColor, Integer glassNum, Date createTime) {
        this.orderId = orderId;
        this.glassHeight = glassHeight;
        this.glassWidth = glassWidth;
        this.glassColor = glassColor;
        this.glassNum = glassNum;
        this.createTime = createTime;
    }
}
