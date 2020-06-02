package com.bingo.erp.xo.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialInfoVO {

    /**
     * 材料颜色
     */
    public int materialColor;

    /**
     * 材料品种
     */
    public int materialType;

    /**
     * 拉手类型
     */
    public int handleType;

    /**
     * 玻璃颜色
     */
    public int glassColor;

    /**
     * 产品高度
     */
    public BigDecimal height;

    /**
     * 产品宽度
     */
    public BigDecimal width;

    private BigDecimal glassHeight;

    private BigDecimal glassWidth;

    private BigDecimal materialHeight;

    private BigDecimal materialWidth;

    /**
     * 产品数量
     */
    public int materialNum;

    /**
     * 拉手位置
     */
    public String handlePlace;


    /**
     * 合页孔位置
     */
    public String hingeLocation;

    /**
     * 开启方向
     */
    public String direction;

    /**
     * 角码种类
     */
    public int cornerMaterial;

    /**
     * 产品下料详情
     */
    private String materialDetail;

    private int cornerNum;

    private int screwNum;

    /**
     * 备注
     */
    public String remark;

    /**
     * 单价
     */
    public BigDecimal price;

    public BigDecimal area;


    public BigDecimal totalPrice;

    public MaterialInfoVO() {
    }

    public MaterialInfoVO(int materialColor,
                          int materialType,
                          int handleType,
                          int glassColor, BigDecimal height, BigDecimal width, BigDecimal glassHeight, BigDecimal glassWidth, BigDecimal materialHeight, BigDecimal materialWidth, int materialNum, String handlePlace, String hingeLocation, String direction, String materialDetail, String remark, BigDecimal price, BigDecimal area, BigDecimal totalPrice) {
        this.materialColor = materialColor;
        this.materialType = materialType;
        this.handleType = handleType;
        this.glassColor = glassColor;
        this.height = height;
        this.width = width;
        this.glassHeight = glassHeight;
        this.glassWidth = glassWidth;
        this.materialHeight = materialHeight;
        this.materialWidth = materialWidth;
        this.materialNum = materialNum;
        this.handlePlace = handlePlace;
        this.hingeLocation = hingeLocation;
        this.direction = direction;
        this.materialDetail = materialDetail;
        this.remark = remark;
        this.price = price;
        this.area = area;
        this.totalPrice = totalPrice;
    }
}
