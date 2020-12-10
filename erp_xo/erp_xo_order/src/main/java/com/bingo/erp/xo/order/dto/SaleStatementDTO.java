package com.bingo.erp.xo.order.dto;

import com.bingo.erp.commons.entity.OrderInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SaleStatementDTO {

    /**
     * 订单总数
     */
    private Integer orderNum = 0;

    /**
     * 价格总数
     */
    private BigDecimal priceNum = new BigDecimal("0");

    /**
     * 门单总数
     */
    private Integer doorOrderNum = 0;

    /**
     * 门单价格总数
     */
    private BigDecimal doorPriceNum = new BigDecimal("0");

    /**
     * 层板灯单总数
     */
    private Integer laminateOrderNum = 0;

    /**
     * 层板灯单价格总数
     */
    private BigDecimal laminatePriceNum = new BigDecimal("0");

    List<OrderInfo> orderInfos;
}
