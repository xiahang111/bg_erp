package com.bingo.erp.commons.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import com.bingo.erp.base.enums.CornerEnums;
import com.bingo.erp.base.enums.HandleEnums;
import com.bingo.erp.base.enums.LightEnums;
import com.bingo.erp.base.enums.MaterialEnums;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_product_material_calculate_record")
public class MaterialCalculateRecord extends SuperEntity<MaterialCalculateRecord> {

    /**
     * 产品uid
     */
    private String productUid;

    /**
     * 产品名称
     */

    private String productName;

    /**
     * 产品竖料长度
     */
    private BigDecimal heightMaterialResult;

    /**
     * 产品下料详细信息
     */
    private String resultDetail;

    /**
     * 产品横料长度
     */
    private BigDecimal widthMaterialResult;

    /**
     * 角度类型 1：直角，2：45度角
     */
    private CornerEnums corner;


    /**
     * 是否带拉手 1：带拉手，2：不带拉手
     */
    private HandleEnums handle;


    /**
     * 是否带灯 1：带灯，2：不带灯
     */
    private LightEnums light;

    public MaterialCalculateRecord() {
    }

    public MaterialCalculateRecord(String productUid,
                                   String productName,
                                   BigDecimal heightMaterialResult,
                                   String resultDetail,
                                   BigDecimal widthMaterialResult,
                                   CornerEnums corner,
                                   LightEnums light) {
        this.productUid = productUid;
        this.productName = productName;
        this.heightMaterialResult = heightMaterialResult;
        this.resultDetail = resultDetail;
        this.widthMaterialResult = widthMaterialResult;
        this.corner = corner;
        this.light = light;
    }
}
