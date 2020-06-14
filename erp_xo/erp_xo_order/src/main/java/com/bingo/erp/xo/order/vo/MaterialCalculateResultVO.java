package com.bingo.erp.xo.order.vo;

import com.bingo.erp.base.enums.CornerEnums;
import com.bingo.erp.base.vo.BaseCalculateResultVO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialCalculateResultVO extends BaseCalculateResultVO {

    /**
     * 产品uid
     */
    private int productUid;

    /**
     * 产品名称
     */

    private String productName;

    /**
     * 产品竖料长度
     */
    private BigDecimal materialHeight;

    /**
     * 产品横料长度
     */
    private BigDecimal materialWidth;

    /**
     * 玻璃长度
     */
    private BigDecimal glassHeight;

    /**
     * 玻璃宽度
     */
    private BigDecimal glassWidth;


    private CornerEnums corner;
    /**
     * 产品下料详细信息
     */
    private String materialDetail;


    public MaterialCalculateResultVO() {
    }

}
