package com.bingo.erp.commons.entity.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import com.bingo.erp.base.enums.MaterialColorEnums;
import com.bingo.erp.base.enums.StoreMaterialResource;
import com.bingo.erp.base.enums.StoreMaterialStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("s_record_info")
public class StoreRecordInfo extends SuperEntity<StoreRecordInfo> {


    private String materialName;

    private MaterialColorEnums materialColor;

    private String specification;

    private String unit;

    private BigDecimal price;

    private BigDecimal materialNum;

    private BigDecimal totalPrice;

    private StoreMaterialResource materialResource;

    private StoreMaterialStatus materialStatus;



}
