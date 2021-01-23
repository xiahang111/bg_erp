package com.bingo.erp.xo.wx.service.impl;

import com.bingo.erp.xo.wx.dto.OrderDTO;
import com.bingo.erp.xo.wx.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public List<OrderDTO> getOrderListByAdminUid(String adminUid) {
        return null;
    }
}
