package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import com.bingo.erp.base.enums.MaterialColorEnums;
import com.bingo.erp.base.enums.TransomTypeEnums;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_transom_info")
public class TransomInfo extends SuperEntity<TransomInfo> {

    @TableField("order_info_uid")
    private String orderInfouid;

    private TransomTypeEnums transomType;

    private MaterialColorEnums transomColor;

    private BigDecimal height;

    private int transomNum;

    private BigDecimal price;

    private BigDecimal totalPrice;

    private String remark;

    public TransomInfo() {
    }

    public TransomInfo(String orderInfouid,
                       TransomTypeEnums transomType,
                       MaterialColorEnums transomColor,
                       BigDecimal height,
                       int transomNum,
                       BigDecimal price,
                       BigDecimal totalPrice,
                       String remark) {
        this.orderInfouid = orderInfouid;
        this.transomType = transomType;
        this.transomColor = transomColor;
        this.height = height;
        this.transomNum = transomNum;
        this.price = price;
        this.totalPrice = totalPrice;
        this.remark = remark;
    }
}
