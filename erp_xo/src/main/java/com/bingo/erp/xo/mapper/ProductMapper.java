package com.bingo.erp.xo.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.Product;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface ProductMapper extends SuperMapper<Product> {

    /**
     * 获取所有产品信息
     */

    @Select("SELECT * FROM t_product")
    List<Product> selectAllProducts();
}
