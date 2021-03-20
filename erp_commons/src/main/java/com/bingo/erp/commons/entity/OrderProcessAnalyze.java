package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import lombok.Data;

/**
 * 订单流程表
 */
@TableName("t_order_process_analyze")
@Data
public class OrderProcessAnalyze extends SuperEntity<OrderProcessAnalyze> {

    private String fileName;
}
