package com.bingo.erp.xo.order.vo;

import lombok.Data;

import java.util.Date;

@Data
public class StatementQueryVO {

    private String  materialColor;

    private String  dateType;

    private String materialName;

    private Boolean isPartName;

    private Boolean isPartColor;

    private Date startTime;

    private Date endTime;
}
