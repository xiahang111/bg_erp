package com.bingo.erp.xo.factory;

import com.bingo.erp.base.enums.*;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.fatocry.material.MaterialCalculateFactory;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.vo.MaterialCalculateResultVO;
import com.bingo.erp.xo.vo.MaterialCalculateVO;
import com.bingo.erp.xo.vo.MaterialInfoVO;

import java.math.BigDecimal;

/**
 * 联动2号计算类
 */
public class XB50CalculateFactory implements MaterialCalculateFactory<MaterialInfoVO> {


    @Override
    public MaterialCalculateResultVO calculate(MaterialInfoVO vo) throws Exception {

        MaterialCalculateResultVO resultVO = new MaterialCalculateResultVO();
        //计算玻璃长度
        int materialType = vo.getMaterialType();

        ProductCalculateEnums productCalculateEnums = ProductCalculateEnums.getByCode(materialType);

        resultVO.setGlassHeight(vo.getHeight().subtract(productCalculateEnums.getHigth()));

        resultVO.setGlassWidth(vo.getWidth().subtract(productCalculateEnums.getWidth()));

        //计算料的长度

        if(vo.getHandleType() != 0 || StringUtils.isNotEmpty(vo.getHandlePlace().trim())){
            throw new MessageException("50斜边没有带拉手的产品哦~");
        }

        resultVO.setMaterialHeight(vo.getHeight());
        resultVO.setMaterialWidth(vo.getWidth().subtract(new BigDecimal(4)));

        String materialDetail = MaterialEnums.heightMaterial.name + resultVO.getMaterialHeight() + "(mm) 2支;" +
                MaterialEnums.widthMaterial.name + resultVO.getMaterialWidth() + "(mm) 2支;";

        resultVO.setMaterialDetail(materialDetail);

        resultVO.setCorner(CornerEnums.RightAngle);



        return resultVO;
    }
}
