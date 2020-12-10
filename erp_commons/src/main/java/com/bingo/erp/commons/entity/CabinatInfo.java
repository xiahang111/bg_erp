package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.bingo.erp.base.entity.SuperEntity;
import com.bingo.erp.base.enums.GlassColor;
import com.bingo.erp.base.enums.MaterialColorEnums;
import com.bingo.erp.base.enums.MaterialTypeEnums;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CabinatInfo extends SuperEntity<CabinatInfo> {

    @TableField("order_info_uid")
    private String orderInfouid;

    /**
     * 名称
     */
    private String cabinatName;

    /**
     * 颜色
     */
    private MaterialColorEnums materialColor;

    /**
     * 门型
     */
    private MaterialTypeEnums materialType;

    /**
     * 玻璃颜色
     */
    private GlassColor glassColor;

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
     * 拉手位置
     */
    private String handlePlace;

    /**
     * 面积
     */
    private BigDecimal area;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 总金额
     */
    private BigDecimal totalPrice;

    /**
     * 备注
     */
    private String remark;
}
