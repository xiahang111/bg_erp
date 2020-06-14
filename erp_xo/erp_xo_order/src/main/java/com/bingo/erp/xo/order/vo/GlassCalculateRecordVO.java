package com.bingo.erp.xo.order.vo;

import com.bingo.erp.commons.entity.ProductCalculateRecord;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class  GlassCalculateRecordVO {


    private String  uid;
    /**
     * 产品id
     */
    private String productUid;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 高度计算结果
     */
    private BigDecimal heightResult;

    /**
     * 宽度计算结果
     */
    private BigDecimal widthResult;

    /**
     * 创建时间
     */
    private Date createTime;

    public GlassCalculateRecordVO(ProductCalculateRecord record) {
        this.uid = record.getUid();
        this.productUid = record.getProductUid();
        this.heightResult = record.getHeightResult();
        this.widthResult = record.getWidthResult();
        this.createTime = record.getCreateTime();
    }
}
