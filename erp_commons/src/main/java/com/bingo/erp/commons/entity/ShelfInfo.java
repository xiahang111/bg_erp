package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import com.bingo.erp.base.enums.MaterialColorEnums;
import lombok.Data;

import java.math.BigDecimal;

@TableName("t_shelf_info")
@Data
public class ShelfInfo extends SuperEntity<ShelfInfo> {

    @TableField("order_info_uid")
    private String orderInfouid;

    /**
     * 博古架名称
     */
    private String shelfName;

    /**
     * 颜色
     */
    private MaterialColorEnums materialColor;

    /**
     * 高度
     */
    private BigDecimal height;

    /**
     * 宽度
     */
    private BigDecimal width;

    /**
     * 深度
     */
    private BigDecimal depth;

    /**
     * 玻璃数量
     */
    private Integer glassNum;

    /**
     * 板数量
     */
    private Integer plateNum;

    /**
     * 组数
     */
    private Integer groupNum;

    /**
     * 单位
     */
    private String unit;

    /**
     * 价格
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
