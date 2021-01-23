package com.bingo.erp.xo.wx.service;

import com.bingo.erp.xo.wx.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    List<OrderDTO> getOrderListByAdminUid(String adminUid);
}
