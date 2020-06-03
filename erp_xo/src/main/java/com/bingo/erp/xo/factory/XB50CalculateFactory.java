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

        //校验角码
        if(!(vo.getCornerMaterial() == CornerMaterialEnums.None.code || vo.getCornerMaterial() == CornerMaterialEnums.None.code)){

            throw new MessageException("50斜边产品没有角码哦，请确认~");

        }

        MaterialCalculateResultVO resultVO = new MaterialCalculateResultVO();
        //计算玻璃长度
        int materialType = vo.getMaterialType();

        ProductCalculateEnums productCalculateEnums = ProductCalculateEnums.getByCode(materialType);

        resultVO.setGlassHeight(vo.getHeight().subtract(productCalculateEnums.getHigth()));

        resultVO.setGlassWidth(vo.getWidth().subtract(productCalculateEnums.getWidth()));

        //计算料的长度


        resultVO.setMaterialHeight(vo.getHeight());
        resultVO.setMaterialWidth(vo.getWidth().subtract(new BigDecimal(4)));

        String materialDetail = "";
        if (vo.getHandleType() == 0 || StringUtils.isEmpty(vo.getHandlePlace().trim())) {
            materialDetail = MaterialEnums.heightMaterial.name + resultVO.getMaterialHeight() + "(mm) " + 2 * vo.getMaterialNum() + "支;" +
                    MaterialEnums.widthMaterial.name + resultVO.getMaterialWidth() + "(mm) " + 2 * vo.getMaterialNum() + "支;";
        } else {

            materialDetail = MaterialEnums.heightMaterial.name + resultVO.getMaterialHeight() + "(mm) " + 1 * vo.getMaterialNum() + "支;" +
                    MaterialEnums.heightHandleMaterial.name + resultVO.getMaterialHeight() + "(mm) " + 1 * vo.getMaterialNum() + "支;" +
                    MaterialEnums.widthMaterial.name + resultVO.getMaterialWidth() + "(mm) " + 2 * vo.getMaterialNum() + "支;";

        }

        resultVO.setMaterialDetail(materialDetail);

        resultVO.setCorner(CornerEnums.RightAngle);

        return resultVO;
    }
}
