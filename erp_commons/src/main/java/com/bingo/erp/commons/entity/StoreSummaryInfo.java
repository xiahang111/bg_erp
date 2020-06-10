package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import com.bingo.erp.base.enums.MaterialColorEnums;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 仓库总表信息
 */
@Data
@TableName("s_summary_info")
public class StoreSummaryInfo extends SuperEntity<StoreSummaryInfo> {

     private String materialName;

     private MaterialColorEnums materialColor;

     private String specification;

     private String unit;

     private BigDecimal price;

     private BigDecimal weight;

     private BigDecimal materialNum;

     private BigDecimal totalWeight;

     private BigDecimal totalPrice;


}
