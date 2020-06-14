package com.bingo.erp.xo.order.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.ProductCalculateRecord;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProductCalculateRecordMapper extends SuperMapper<ProductCalculateRecord> {

    @Select("SELECT * FROM t_product_glass_calculate_record ORDER BY create_time DESC")
    List<ProductCalculateRecord> getAllGlassCalculateRecord();

}
