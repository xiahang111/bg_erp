package com.bingo.erp.xo.order.vo;

import lombok.Data;

@Data
public class StoreStatementVO {

    /**
     * 料颜色
     */
    private String  materialColor;

    /**
     * 料名称
     */
    private String materialName;

    /**
     * 料种类 1 成品料 2 坯料
     */
    private Integer materialType = 1;

    /**
     * 日期类型 1 年 2 月 3 日 默认为3
     */
    private String  dateType = "3";

    /**
     * 是否区分料名称 默认不区分
     */
    private Boolean isPartName = false;

    /**
     * 是否区分颜色 默认不区分
     */
    private Boolean isPartColor =  false;
}
