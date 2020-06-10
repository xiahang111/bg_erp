package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import com.bingo.erp.base.enums.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_laminate_info")
public class LaminateInfo extends SuperEntity<LaminateInfo> {

    @TableField("order_info_uid")
    private String orderInfouId;

    private MaterialColorEnums materialColor;

    private MaterialTypeEnums materialType;

    private GlassColor glassColor;

    private BigDecimal width;

    private BigDecimal glassWidth;

    private BigDecimal depth;

    private BigDecimal glassDepth;

    private Integer laminateNum;

    private String lightPlace;

    private String linePlace;

    private LightColor lightColor;

    private LineColor lineColor;

    private BigDecimal perimeter;

    private BigDecimal price;

    private BigDecimal totalPrice;

    private String remark;

    public LaminateInfo() {
    }

    public LaminateInfo(String orderInfouId, MaterialColorEnums materialColor, MaterialTypeEnums materialType, GlassColor glassColor, BigDecimal width, BigDecimal glassWidth, BigDecimal depth, BigDecimal glassDepth, Integer laminateNum, String lightPlace, String linePlace, LightColor lightColor, LineColor lineColor, BigDecimal perimeter, BigDecimal price, BigDecimal totalPrice, String remark) {
        this.orderInfouId = orderInfouId;
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
        this.perimeter = perimeter;
        this.price = price;
        this.totalPrice = totalPrice;
        this.remark = remark;
    }
}
