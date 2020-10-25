package com.bingo.erp.xo.order.service.impl;

import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.OrderGlassDetail;
import com.bingo.erp.xo.order.mapper.OrderGlassDetailMapper;
import com.bingo.erp.xo.order.service.OrderGlassDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderGlassDetailServiceImpl extends SuperServiceImpl<OrderGlassDetailMapper,OrderGlassDetail> implements OrderGlassDetailService {
}
