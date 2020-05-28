package com.bingo.erp.xo.factory;

import com.bingo.erp.base.enums.CornerEnums;
import com.bingo.erp.base.enums.HandleEnums;
import com.bingo.erp.base.enums.LightEnums;
import com.bingo.erp.base.enums.MaterialEnums;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.fatocry.material.MaterialCalculateFactory;
import com.bingo.erp.xo.vo.MaterialCalculateResultVO;
import com.bingo.erp.xo.vo.MaterialCalculateVO;
import com.bingo.erp.xo.vo.MaterialInfoVO;

import java.math.BigDecimal;

/**
 * 层板灯计算类
 */
public class CBDCalculateFactory implements MaterialCalculateFactory<MaterialInfoVO> {


    @Override
    public MaterialCalculateResultVO calculate(MaterialInfoVO vo) throws Exception {

        //todo 层板灯数据计算逻辑
        return new MaterialCalculateResultVO();
    }
}
