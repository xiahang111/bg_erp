package com.bingo.erp.xo.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.OrderInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderInfoMapper extends SuperMapper<OrderInfo> {


    @Select("SELECT * FROM t_order_info ORDER BY create_time DESC limit 10")
    List<OrderInfo> getIndexOrderInfo();
}
