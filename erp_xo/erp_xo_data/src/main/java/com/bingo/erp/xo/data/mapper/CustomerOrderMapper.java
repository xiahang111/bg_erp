package com.bingo.erp.xo.data.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.xo.data.dto.CustomerTopDTO;
import com.bingo.erp.xo.data.entity.CustomerOrder;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CustomerOrderMapper extends SuperMapper<CustomerOrder> {


    @Select("select SUM(total_price) as totalPrice ,customer_uid as customerUid from t_customer_order_mapper group by customer_uid ORDER BY SUM(total_price) desc limit 10")
    List<CustomerTopDTO> getCustomerTop();
}
