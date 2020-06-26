package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import com.bingo.erp.base.enums.MaterialColorEnums;
import com.bingo.erp.base.enums.StoreMaterialResource;
import com.bingo.erp.base.enums.StoreMaterialStatus;
import com.bingo.erp.base.enums.StoreOriginalResource;
import lombok.Data;

import java.math.BigDecimal;

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

    private StoreOriginalResource storeOriginalResource;

    private StoreMaterialStatus materialStatus;
}
