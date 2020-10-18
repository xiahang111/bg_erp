package com.bingo.erp.xo.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreDataDTO {

    /**
     * 库存数据种类 1：坯料数据 2：成品料数据
     */
    private String  storeType;
    /**
     * 仓库总数量
     */
    private int storeTotalNum;
    /**
     * 仓库总价值
     */
    private BigDecimal storeTotalPrice = new BigDecimal("0");
    /**
     * 仓库总重量
     */
    private BigDecimal storeTotalWeight = new BigDecimal("0");

    /**
     * 仓库总种类数量
     */
    private int storeTotalType;

    public StoreDataDTO() {
    }

    public StoreDataDTO(String storeType) {
        this.storeType = storeType;
    }
}