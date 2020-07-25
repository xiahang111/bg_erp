package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import com.bingo.erp.base.enums.MaterialStatusEnums;
import com.bingo.erp.base.enums.StoreMaterialStatus;
import com.bingo.erp.base.enums.StoreOriginalResource;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("s_origin_out_info")
public class StoreOriginalOutInfo extends SuperEntity<StoreOriginalOutInfo> {

    private StoreOriginalResource originalResource;

    private String materialName;

    private MaterialStatusEnums materialStatus;

    private StoreMaterialStatus storeMaterialStatus;

    private String specification;

    private String unit;

    private BigDecimal materialNum;



}
