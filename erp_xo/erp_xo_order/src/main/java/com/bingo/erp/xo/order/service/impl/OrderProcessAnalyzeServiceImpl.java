package com.bingo.erp.xo.order.service.impl;

import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.OrderProcessAnalyze;
import com.bingo.erp.xo.order.mapper.OrderProcessAnalyzeMapper;
import com.bingo.erp.xo.order.service.OrderProcessAnalyzeService;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessAnalyzeServiceImpl extends
        SuperServiceImpl<OrderProcessAnalyzeMapper, OrderProcessAnalyze> implements OrderProcessAnalyzeService {
}
