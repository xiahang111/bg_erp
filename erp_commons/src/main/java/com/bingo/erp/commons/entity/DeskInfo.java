package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import com.bingo.erp.base.enums.MaterialColorEnums;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 极简餐桌
 */
@TableName("t_desk_info")
@Data
public class DeskInfo extends SuperEntity<DeskInfo> {

    @TableField("order_info_uid")
    private String orderInfouid;

    /**
     *桌腿颜色
     */
    private MaterialColorEnums materialColor;

    /**
     * 长度
     */
    private BigDecimal length;

    /**
     * 宽度
     */
    private BigDecimal width;

    /**
     * 高度
     */
    private BigDecimal height;

    /**
     * 桌子数量
     */
    private Integer deskNum;

    /**
     * 桌子单价
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
