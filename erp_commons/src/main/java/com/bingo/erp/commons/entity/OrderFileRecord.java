package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import lombok.Data;

/**
 * 订单及文件对应关系表
 */
@TableName("t_order_file")
@Data
public class OrderFileRecord extends SuperEntity<OrderFileRecord> {

    /**
     * 订单主键uid
     */
    private String orderUid;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 订单对应文件名
     */
    private String fileName;
}
