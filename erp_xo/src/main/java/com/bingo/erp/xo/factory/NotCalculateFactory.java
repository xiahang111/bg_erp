package com.bingo.erp.xo.factory;

import com.bingo.erp.base.enums.CornerEnums;
import com.bingo.erp.base.enums.HandleEnums;
import com.bingo.erp.base.enums.LightEnums;
import com.bingo.erp.base.enums.MaterialEnums;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.fatocry.material.MaterialCalculateFactory;
import com.bingo.erp.xo.vo.MaterialCalculateResultVO;
import com.bingo.erp.xo.vo.MaterialCalculateVO;

import java.math.BigDecimal;

/**
 * 不计算直接抛出异常
 */
public class NotCalculateFactory implements MaterialCalculateFactory<MaterialCalculateVO> {


    @Override
    public MaterialCalculateResultVO calculate(MaterialCalculateVO vo) throws Exception {

        throw new MessageException("该产品暂无下料信息哦~");

    }
}
