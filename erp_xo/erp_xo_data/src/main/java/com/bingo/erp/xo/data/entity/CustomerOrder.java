package com.bingo.erp.xo.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_customer_order_mapper")
public class CustomerOrder extends SuperEntity<CustomerOrder> {


    @TableField("customer_uid")
    private String customerUid;

    @TableField("order_uid")
    private String orderUid;

    private String orderId;

    private BigDecimal totalPrice;

}
