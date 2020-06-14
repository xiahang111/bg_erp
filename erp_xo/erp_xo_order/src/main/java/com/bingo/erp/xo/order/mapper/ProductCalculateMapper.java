package com.bingo.erp.xo.order.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.ProductCalculate;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProductCalculateMapper extends SuperMapper<ProductCalculate> {


    @Select("SELECT * FROM t_product_glass_calculate")
    List<ProductCalculate> getAllProductCalculate();

}
