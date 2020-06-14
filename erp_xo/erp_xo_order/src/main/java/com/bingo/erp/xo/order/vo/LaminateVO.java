package com.bingo.erp.xo.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class LaminateVO extends ProductVO {

    /**
     * 五金信息
     */
    private List<IronwareInfoVO> ironwares;

    /**
     *
     */
    private BigDecimal ironTotalPrice;


    /**
     * 层板灯信息
     */
    private List<LaminateInfoVO> laminateInfos;


    /**
     * 层板灯总价格
     */
    private BigDecimal laminateTotalPrice;


}
