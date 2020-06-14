package com.bingo.erp.xo.order.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProductMapper extends SuperMapper<Product> {

    /**
     * 获取所有产品信息
     */

    @Select("SELECT * FROM t_product WHERE product_type = #{productType}")
    List<Product> selectAllProducts(@Param("productType") Integer productType);
}
