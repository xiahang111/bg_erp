package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_specimen_info")
public class SpecimenInfo extends SuperEntity<SpecimenInfo> {

    @TableField("order_info_uid")
    private String orderInfouid;

    /**
     * 小样品名称
     */
    private String specimenName;

    /**
     * 规格
     */
    private String specification;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 小样品数量
     */
    private Integer specimenNum;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 包装数量
     */
    private Integer packageNum;

    /**
     * 备注
     */
    private String remark;



}
