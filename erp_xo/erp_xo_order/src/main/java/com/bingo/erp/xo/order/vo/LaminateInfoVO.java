package com.bingo.erp.xo.order.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LaminateInfoVO {

    /**
     * 材料颜色
     */
    public int materialColor;

    /**
     * 材料品种
     */
    public int materialType;

    /**
     * 玻璃颜色
     */
    public int glassColor;

    private BigDecimal width;

    private BigDecimal glassWidth;

    private BigDecimal depth;

    private BigDecimal glassDepth;

    private Integer laminateNum;

    private String lightPlace;

    private String linePlace;

    private int lightColor = 0;

    private int lineColor = 0;

    private BigDecimal price;

    private BigDecimal totalPrice;

    /**
     * 管道类型 双管or单管
     */
    private String pipeType = "/";

    private String remark = "";

    /**
     * 周长
     */
    private BigDecimal perimeter;

    public LaminateInfoVO() {
    }

    public LaminateInfoVO(int materialColor, int materialType, int glassColor, BigDecimal width, BigDecimal glassWidth, BigDecimal depth, BigDecimal glassDepth, Integer laminateNum, String lightPlace, String linePlace, Integer lightColor, Integer lineColor, BigDecimal price, BigDecimal totalPrice, String remark, BigDecimal perimeter) {
        this.materialColor = materialColor;
        this.materialType = materialType;
        this.glassColor = glassColor;
        this.width = width;
        this.glassWidth = glassWidth;
        this.depth = depth;
        this.glassDepth = glassDepth;
        this.laminateNum = laminateNum;
        this.lightPlace = lightPlace;
        this.linePlace = linePlace;
        this.lightColor = lightColor;
        this.lineColor = lineColor;
        this.price = price;
        this.totalPrice = totalPrice;
        this.remark = remark;
        this.perimeter = perimeter;
    }
}
