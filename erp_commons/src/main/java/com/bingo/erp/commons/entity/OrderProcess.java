package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import com.bingo.erp.base.enums.OrderStatusEnums;
import lombok.Data;

import java.util.Date;

/**
 * 订单流程表
 */
@TableName("t_order_process")
@Data
public class OrderProcess extends SuperEntity<OrderProcess> {

    @TableField("order_info_uid")
    private String orderInfouid;

    private String orderId;

    private OrderStatusEnums orderStatus;

    private Date orderDate;

    private Date deliveryDate;

    private String  restPrice;

    private String orderType;

    /**
     * 订单内制作货物的数量
     */
    private String materialNum;

    private Boolean glassHave;

    private Boolean glassArrive;

    /**
     * 下料日期
     */
    private Date materialDate;

    /**
     * 数控日期
     */
    private Date digitalDate;

    /**
     * 台钻日期
     */
    private Date drillDate;

    /**
     * 组装日期
     */
    private Date assembleDate;

    /**
     * 包装日期
     */
    private Date packageDate;


    private String packageNum;

    private String remark;

    private String customerName;

    private String customerPhone;

    private String customerAddr;

    private String express;

    private String expressReal;

    private String expressId;

    private Date deliveryDateReal;

    @Override
    public String toString() {
        return "OrderProcess{" +
                "orderInfouid='" + orderInfouid + '\'' +
                ", orderId='" + orderId + '\'' +
                ", orderStatus=" + orderStatus +
                ", orderDate=" + orderDate +
                ", deliveryDate=" + deliveryDate +
                ", restPrice=" + restPrice +
                ", orderType=" + orderType +
                ", materialNum='" + materialNum + '\'' +
                ", glassHave=" + glassHave +
                ", glassArrive=" + glassArrive +
                ", materialDate=" + materialDate +
                ", digitalDate=" + digitalDate +
                ", drillDate=" + drillDate +
                ", assembleDate=" + assembleDate +
                ", packageDate=" + packageDate +
                ", packageNum='" + packageNum + '\'' +
                ", remark='" + remark + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", customerAddr='" + customerAddr + '\'' +
                ", express='" + express + '\'' +
                ", expressReal='" + expressReal + '\'' +
                ", expressId='" + expressId + '\'' +
                ", deliveryDateReal=" + deliveryDateReal +
                '}';
    }
}
