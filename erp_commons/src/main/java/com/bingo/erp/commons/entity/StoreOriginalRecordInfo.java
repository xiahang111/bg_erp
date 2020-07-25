package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import com.bingo.erp.base.enums.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("s_original_record_info")
public class StoreOriginalRecordInfo extends SuperEntity<StoreOriginalRecordInfo> {

    private String materialName;

    private String specification;

    private String unit;

    private BigDecimal price;

    private BigDecimal materialNum;

    private BigDecimal aluPrice;

    private BigDecimal totalPrice;

    private StoreOriginalResource originalResource;

    private StoreMaterialStatus storeMaterialStatus;

    private MaterialStatusEnums materialStatus;

    /**
     * 订单日期
     */
    private Date orderDate;

    /**
     * 理论支重
     */
    private BigDecimal weight;

    /**
     * 重量
     */
    private BigDecimal totalWeight;

}
