package com.bingo.erp.xo.order.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StoreRecordDTO {

    private Date dateTime;

    private String materialStatus;

    private BigDecimal totalNum;

    private BigDecimal totalPrice;

    private String materialName;

    private Integer materialColor;
}
