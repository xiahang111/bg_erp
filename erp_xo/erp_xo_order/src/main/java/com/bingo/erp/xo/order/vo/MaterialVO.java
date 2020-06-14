package com.bingo.erp.xo.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MaterialVO extends ProductVO{

    /**
     * 材料信息
     */
    public List<MaterialInfoVO> materials;

    /**
     * 五金信息
     */
    public List<IronwareInfoVO> ironwares;

    /**
     *
     */
    public BigDecimal ironTotalPrice;

    /**
     * 门框玻璃总信息
     */
    public BigDecimal materialTotalprice;


    /**
     * 是否含有天地横梁
     */
    public Boolean isHaveTransom;

    /**
     * 天地横梁信息
     */
    public List<TransomVO> transoms;
}
