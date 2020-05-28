package com.bingo.erp.base.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 计算方法父类 约定使用计算方法时的产品类型
 */
@Data
public class BaseCalculateVO {

    public BigDecimal height;

    public BigDecimal width;
}
