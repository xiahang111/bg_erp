package com.bingo.erp.base.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

//向person微服务保存客户信息的vo
@Data
public class CustomerVO implements Serializable {

    private String adminUid;

    private String customerUid;

    private String orderUid;

    private String orderId;

    private BigDecimal totalPrice;

    private String customerName;

    private String customerAddr;

    private String nameMapper;

    private String cutomerPhone;

    private String salesman;

    private Integer customerResource;

    private String customerNick;

    private String express;

}
