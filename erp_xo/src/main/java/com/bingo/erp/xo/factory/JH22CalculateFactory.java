package com.bingo.erp.xo.factory;

import com.bingo.erp.base.enums.CornerEnums;
import com.bingo.erp.base.enums.MaterialEnums;
import com.bingo.erp.base.enums.ProductCalculateEnums;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.fatocry.material.MaterialCalculateFactory;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.vo.MaterialCalculateResultVO;
import com.bingo.erp.xo.vo.MaterialInfoVO;

import java.math.BigDecimal;

/**
 * 联动2号计算类
 */
public class JH22CalculateFactory implements MaterialCalculateFactory<MaterialInfoVO> {


    @Override
    public MaterialCalculateResultVO calculate(MaterialInfoVO vo) throws Exception {

        MaterialCalculateResultVO resultVO = new MaterialCalculateResultVO();
        //计算玻璃长度
        int materialType = vo.getMaterialType();

        ProductCalculateEnums productCalculateEnums = ProductCalculateEnums.getByCode(materialType);

        resultVO.setGlassHeight(vo.getHeight().subtract(productCalculateEnums.getHigth()));

        resultVO.setGlassWidth(vo.getWidth().subtract(productCalculateEnums.getWidth()));

        //计算料的长度

        if(vo.getHandleType() == 0 || StringUtils.isEmpty(vo.getHandlePlace().trim())){
            throw new MessageException("20窄边没有不带拉手的产品哦~");
        }

        resultVO.setMaterialHeight(vo.getHeight());
        resultVO.setMaterialWidth(vo.getWidth().subtract(new BigDecimal(0)));


        String materialDetail = MaterialEnums.heightMaterial.name + resultVO.getMaterialHeight() + "(mm) 1支;" +
                MaterialEnums.heightLightMaterial.name + resultVO.getMaterialHeight()+"(mm) 1支;"+
                MaterialEnums.widthMaterial.name + resultVO.getMaterialWidth() + "(mm) 2支;";

        resultVO.setMaterialDetail(materialDetail);

        resultVO.setCorner(CornerEnums.BevelAngle);



        return resultVO;
    }
}
