package com.bingo.erp.xo.order.vo;

import lombok.Data;

@Data
public class RemindConfigVO {

    private String uid;

    private String materialName;

    private String specification;

    private Integer materialColor;

    private Integer remindMethod;

    private Integer threshold;
}
