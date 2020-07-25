package com.bingo.erp.xo.order.factory;

import com.bingo.erp.base.enums.MaterialTypeEnums;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.fatocry.material.MaterialCalculateFactory;
import com.bingo.erp.xo.order.vo.MaterialCalculateResultVO;
import com.bingo.erp.xo.order.vo.MaterialCalculateVO;
import com.bingo.erp.xo.order.vo.MaterialInfoVO;


/**
 * 不计算直接抛出异常
 */
public class NotCalculateFactory implements MaterialCalculateFactory<MaterialInfoVO> {


    @Override
    public MaterialCalculateResultVO calculate(MaterialInfoVO vo) throws Exception {
        String materialName = MaterialTypeEnums.getEnumByCode(vo.getMaterialType());
        throw new MessageException(materialName + "暂无下料信息哦~");

    }
}
