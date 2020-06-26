package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 坯料信息总表
 */
@Data
@TableName("s_original_info")
public class StoreOriginalInfo extends SuperEntity<StoreOriginalInfo> {

    private String materialName;

    private String specification;

    private String unit;

    private BigDecimal weight;

    private BigDecimal price;

    private BigDecimal materialNum;

    private BigDecimal totalWeight;

    private BigDecimal totalPrice;

    private BigDecimal useless;
}
