package com.bingo.erp.xo.order.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.OrderInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderInfoMapper extends SuperMapper<OrderInfo> {


    @Select("SELECT * FROM t_order_info ORDER BY create_time DESC limit 10")
    List<OrderInfo> getIndexOrderInfo();

    @Select("SELECT ORDER_ID FROM t_order_info WHERE uid = #{orderInfoUid}")
    String getOrderIdByUid(@Param("orderInfoUid") String orderInfoUid);
}
