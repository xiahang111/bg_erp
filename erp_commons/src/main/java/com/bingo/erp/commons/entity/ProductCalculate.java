package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_product_glass_calculate")
public class ProductCalculate extends SuperEntity<ProductCalculate> {

    private static final long serialVersionUID = 1L;

    /**
     * 产品id
     */
    private String  productUid;

    /**
     * 需要减去的高度
     */
    private BigDecimal height;


    /**
     * 需要减去的宽度
     */
    private BigDecimal width;



}


