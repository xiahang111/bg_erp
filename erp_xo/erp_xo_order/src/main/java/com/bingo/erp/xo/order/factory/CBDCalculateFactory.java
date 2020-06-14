package com.bingo.erp.xo.order.factory;

import com.bingo.erp.base.enums.ProductCalculateEnums;
import com.bingo.erp.base.fatocry.material.MaterialCalculateFactory;
import com.bingo.erp.xo.order.vo.LaminateInfoVO;
import com.bingo.erp.xo.order.vo.MaterialCalculateResultVO;


/**
 * 层板灯计算类
 */
public class CBDCalculateFactory implements MaterialCalculateFactory<LaminateInfoVO> {


    @Override
    public MaterialCalculateResultVO calculate(LaminateInfoVO vo) throws Exception {

        MaterialCalculateResultVO resultVO = new MaterialCalculateResultVO();
        int materialType = vo.getMaterialType();

        ProductCalculateEnums productCalculateEnums = ProductCalculateEnums.getByCode(materialType);

        resultVO.setGlassHeight(vo.getWidth().subtract(productCalculateEnums.getHigth()));

        resultVO.setGlassWidth(vo.getDepth().subtract(productCalculateEnums.getWidth()));
        //todo 层板灯数据计算逻辑
        return resultVO;
    }
}
