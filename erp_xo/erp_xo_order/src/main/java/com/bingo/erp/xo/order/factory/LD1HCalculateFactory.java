package com.bingo.erp.xo.order.factory;

import com.bingo.erp.base.enums.CornerEnums;
import com.bingo.erp.base.enums.MaterialEnums;
import com.bingo.erp.base.enums.ProductCalculateEnums;
import com.bingo.erp.base.fatocry.material.MaterialCalculateFactory;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.order.vo.MaterialCalculateResultVO;
import com.bingo.erp.xo.order.vo.MaterialInfoVO;

import java.math.BigDecimal;

/**
 * 联动1号计算类
 */
public class LD1HCalculateFactory implements MaterialCalculateFactory<MaterialInfoVO> {


    @Override
    public MaterialCalculateResultVO calculate(MaterialInfoVO vo) throws Exception {

        MaterialCalculateResultVO resultVO = new MaterialCalculateResultVO();
        //计算玻璃长度
        int materialType = vo.getMaterialType();

        ProductCalculateEnums productCalculateEnums = ProductCalculateEnums.getByCode(materialType);

        resultVO.setGlassHeight(vo.getHeight().subtract(productCalculateEnums.getHigth()).setScale(4,BigDecimal.ROUND_HALF_DOWN));

        resultVO.setGlassWidth(vo.getWidth().subtract(productCalculateEnums.getWidth()).setScale(4,BigDecimal.ROUND_HALF_DOWN));

        //计算料的长度
        resultVO.setMaterialHeight(vo.getHeight());
        resultVO.setMaterialWidth(vo.getWidth().subtract(new BigDecimal(30)));

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
