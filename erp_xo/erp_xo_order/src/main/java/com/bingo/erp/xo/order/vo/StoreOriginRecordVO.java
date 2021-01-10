package com.bingo.erp.xo.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StoreOriginRecordVO {

    /**
     * 坯料来源
     */
    private Integer originalResource;
    /**
     * 产品名称
     */
    private String materialName;

    /**
     * 订单日期
     */
    private Date orderDate;

    /**
     * 理论支重
     */
    private BigDecimal weight;

    /**
     * 总重量
     */
    private BigDecimal totalWeight;

    private String unit;

    private String specification;

    /**
     * 支数
     */
    private BigDecimal materialNum;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 铝锭价格
     */
    private BigDecimal aluPrice;

    /**
     * 总金额
     */
    private BigDecimal totalPrice;

    /**
     * 材料所处状态
     */
    private Integer materialStatus;

    private Integer storeMaterialStatus;

    private Integer materialColor;


}
