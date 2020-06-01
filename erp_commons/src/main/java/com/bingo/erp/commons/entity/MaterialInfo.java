package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import com.bingo.erp.base.enums.*;
import lombok.Data;

import java.math.BigDecimal;

@TableName("t_material_info")
@Data
public class MaterialInfo extends SuperEntity<MaterialInfo> {

    @TableField("order_info_uid")
    private String orderInfouId;

    private MaterialColorEnums materialColor;

    private MaterialTypeEnums materialType;

    private HandleEnums handleType;

    /**
     * 合页孔位置
     */
    public String hingeLocation;

    private GlassColor glassColor;

    private BigDecimal glassHeight;

    private BigDecimal glassWidth;

    private BigDecimal height;

    private BigDecimal width;

    private BigDecimal materialHeight;

    private BigDecimal materialWidth;

    private Integer materialNum;

    private String handlePlace;

    private String  direction;

    private String materialDetail;

    private String remark;

    private BigDecimal price;

    private BigDecimal area;

    private BigDecimal totalPrice;

    public MaterialInfo() {
    }

    public MaterialInfo(String orderInfoUId,
                        MaterialColorEnums materialColor,
                        MaterialTypeEnums materialType,
                        HandleEnums handleType,
                        String hingeLocation,
                        GlassColor glassColor,
                        BigDecimal glassHeight,
                        BigDecimal glassWidth,
                        BigDecimal height,
                        BigDecimal width,
                        BigDecimal materialHeight,
                        BigDecimal materialWidth,
                        Integer materialNum,
                        String handlePlace,
                        String  direction,
                        String materialDetail,
                        String remark,
                        BigDecimal price,
                        BigDecimal area,
                        BigDecimal totalPrice) {
        this.orderInfouId = orderInfoUId;
        this.materialColor = materialColor;
        this.materialType = materialType;
        this.handleType = handleType;
        this.hingeLocation = hingeLocation;
        this.glassColor = glassColor;
        this.glassHeight = glassHeight;
        this.glassWidth = glassWidth;
        this.height = height;
        this.width = width;
        this.materialHeight = materialHeight;
        this.materialWidth = materialWidth;
        this.materialNum = materialNum;
        this.handlePlace = handlePlace;
        this.direction = direction;
        this.materialDetail = materialDetail;
        this.remark = remark;
        this.price = price;
        this.area = area;
        this.totalPrice = totalPrice;
    }
}
