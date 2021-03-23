package com.bingo.erp.commons.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单玻璃数据表
 */
@TableName("t_order_glass_detail")
@Data
public class OrderGlassDetail extends SuperEntity<OrderFileRecord> {


    private String orderUid;
    /**
    原单号的id
     */
    private String materialUid;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 原订单类型
     */
    private String materialType;

    private String glassColor;

    /**
     * 玻璃细节描述
     */
    private String glassDetail;

    private BigDecimal glassHeight;

    private BigDecimal glassWidth;

    private Integer materialNum;

    private String customerName;

    private Integer sequence;

}
