package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import lombok.Data;

@Data
@TableName("t_product")
public class Product extends SuperEntity<Product> {

    private static final long serialVersionUID = 1L;

    /**
     * 产品名称
     */
    private String productName;

}
