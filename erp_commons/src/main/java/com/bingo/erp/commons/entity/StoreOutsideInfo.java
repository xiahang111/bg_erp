package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import com.bingo.erp.base.enums.MaterialColorEnums;
import com.bingo.erp.base.enums.MaterialStatusEnums;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 在外材料信息表
 */
@TableName("s_store_outside_info")
@Data
public class StoreOutsideInfo extends SuperEntity<StoreOutsideInfo> {


    /**
     * 原单号uid
     */
    private String originUid;

    /**
     * 材料名称
     */
    private String materialName;

    /**
     * 规格
     */
    private String specification;

    /**
     * 单位
     */
    private String unit;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 总金额
     */
    private BigDecimal totalPrice;

    /**
     * 材料数量
     */
    private Integer materialNum;

    /**
     * 材料状态
     */
    private MaterialStatusEnums materialStatus;

    /**
     * 材料颜色
     */
    private MaterialColorEnums materialColor;

    /**
     * 所在厂家
     */
    private String location;

    /**
     * 发出时间
     */
    private Date outTime;

    /**
     * 预计完成时间
     */
    private Date completeTime;

    /**
     * 备注
     */
    private String remark;


}
