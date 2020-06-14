package com.bingo.erp.xo.order.factory;

import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.fatocry.material.MaterialCalculateFactory;
import com.bingo.erp.xo.order.vo.MaterialCalculateResultVO;
import com.bingo.erp.xo.order.vo.MaterialCalculateVO;


/**
 * 不计算直接抛出异常
 */
public class NotCalculateFactory implements MaterialCalculateFactory<MaterialCalculateVO> {


    @Override
    public MaterialCalculateResultVO calculate(MaterialCalculateVO vo) throws Exception {

        throw new MessageException("该产品暂无下料信息哦~");

    }
}
