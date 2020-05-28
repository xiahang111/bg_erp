package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_product_glass_calculate_record")
public class ProductCalculateRecord extends SuperEntity<ProductCalculateRecord> {


    /**
     * 产品id
     */
    private String productUid;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 宽度计算结果
     */
    private BigDecimal widthResult;

    /**
     * 高度计算结果
     */
    private BigDecimal heightResult;

    public ProductCalculateRecord() {
    }

    public ProductCalculateRecord(String  productUid, String productName, BigDecimal widthResult, BigDecimal heightResult) {
        this.productUid = productUid;
        this.productName = productName;
        this.widthResult = widthResult;
        this.heightResult = heightResult;
    }
}
