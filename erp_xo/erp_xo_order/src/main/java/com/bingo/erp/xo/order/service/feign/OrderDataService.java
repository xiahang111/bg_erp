package com.bingo.erp.xo.order.service.feign;

import com.bingo.erp.xo.order.dto.OrderDTO;

import java.util.List;

public interface OrderDataService {

    List<OrderDTO> getOrderByAdminUid(String adminUid);
}
