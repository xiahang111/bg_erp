package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import com.bingo.erp.base.enums.MaterialColorEnums;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 置物架
 */
@TableName("t_hanging_info")
@Data
public class HangingInfo extends SuperEntity<HangingInfo> {

    @TableField("order_info_uid")
    private String orderInfouid;

    /**
     * 置物架名称
     */
    private String hangingName;

    /**
     * 规格
     */
    private String specification;

    /**
     * 颜色
     */
    private MaterialColorEnums materialColor;

    /**
     * 数量
     */
    private Integer hangingNum;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 总价格
     */
    private BigDecimal totalPrice;

    /**
     * 备注
     */
    private String remark;


}
