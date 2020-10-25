package com.bingo.erp.xo.order.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 获取数据统计
 */
@Data
public class OrderStaticDataDTO {

    /**
     * 当年的销售额
     */
    private String yearPriceCount;

    /**
     * 当年的订单数
     */
    private String yearOrderNum;

    /**
     * 当月的销售额
     */
    private String monthPriceCount;

    /**
     * 当月的订单数
     */
    private String monthOrderNum;

    /**
     * 当天的销售额
     */
    private String dayPriceCount;

    /**
     * 当天的订单数
     */
    private String dayOrderNum;

}
