package com.bingo.erp.xo.order.factory;

import com.bingo.erp.base.enums.CornerEnums;
import com.bingo.erp.base.enums.CornerMaterialEnums;
import com.bingo.erp.base.enums.MaterialEnums;
import com.bingo.erp.base.enums.ProductCalculateEnums;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.fatocry.material.MaterialCalculateFactory;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.order.vo.MaterialCalculateResultVO;
import com.bingo.erp.xo.order.vo.MaterialInfoVO;


import java.math.BigDecimal;

/**
 * 联动2号计算类
 */
public class LD2HCalculateFactory implements MaterialCalculateFactory<MaterialInfoVO> {


    @Override
    public MaterialCalculateResultVO calculate(MaterialInfoVO vo) throws Exception {


        //校验角码
        if(!(vo.getCornerMaterial() == CornerMaterialEnums.LD2HTPJM.code|| vo.getCornerMaterial() == CornerMaterialEnums.None.code)){

            throw new MessageException("角码种类与产品种类不匹配哦~请确认");

        }

        MaterialCalculateResultVO resultVO = new MaterialCalculateResultVO();
        //计算玻璃长度
        int materialType = vo.getMaterialType();

        ProductCalculateEnums productCalculateEnums = ProductCalculateEnums.getByCode(materialType);

        resultVO.setGlassHeight(vo.getHeight().subtract(productCalculateEnums.getHigth()));

        resultVO.setGlassWidth(vo.getWidth().subtract(productCalculateEnums.getWidth()));

        //计算料的长度

        if(vo.getHandleType() != 0 || StringUtils.isNotEmpty(vo.getHandlePlace().trim())){
            throw new MessageException("联动2号没有带拉手的产品哦~");
        }

        resultVO.setMaterialHeight(vo.getHeight());
        resultVO.setMaterialWidth(vo.getWidth().subtract(new BigDecimal(0)));

        String materialDetail = MaterialEnums.heightMaterial.name + resultVO.getMaterialHeight() + "(mm) "+ 2 * vo.getMaterialNum() +"支;" +
                MaterialEnums.widthMaterial.name + resultVO.getMaterialWidth() + "(mm) "+ 2 * vo.getMaterialNum() +"支;";

        resultVO.setMaterialDetail(materialDetail);

        resultVO.setCorner(CornerEnums.BevelAngle);



        return resultVO;
    }
}
