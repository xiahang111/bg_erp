package com.bingo.erp.xo.order.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SaleStatementVO {

    private String salemen;

    private String ordermaker;

    private String productType;

    private String orderType;

    private List<Date> dateScope;
}
