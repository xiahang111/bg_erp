package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import com.bingo.erp.base.enums.MaterialColorEnums;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_metal_info")
public class MetalInfo extends SuperEntity<MetalInfo> {

    @TableField("order_info_uid")
    private String orderInfouid;

    private String metalName;

    /**
     * 颜色
     */
    private MaterialColorEnums materialColor;

    private String specification;

    private Integer metalNum;

    private BigDecimal price;

    private BigDecimal totalPrice;

    private String remark;

}
