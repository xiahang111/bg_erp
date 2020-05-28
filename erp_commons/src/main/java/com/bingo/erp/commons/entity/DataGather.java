package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_data_gather")
public class DataGather extends SuperEntity<DataGather> {

    /**
     * 今日订单数量
     */
    private BigDecimal dayOrderNums;

    /**
     * 本月订单数量
     */
    private BigDecimal monthOrderNums;


    /**
     * 今日销售额
     */
    private BigDecimal daySaleNums;


    /**
     * 本月销售额
     */
    private BigDecimal monthSaleNums;
}
