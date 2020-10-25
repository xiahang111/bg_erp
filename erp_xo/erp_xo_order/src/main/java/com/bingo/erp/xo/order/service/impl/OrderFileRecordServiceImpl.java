package com.bingo.erp.xo.order.service.impl;

import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.OrderFileRecord;
import com.bingo.erp.xo.order.mapper.OrderFileRecordMapper;
import com.bingo.erp.xo.order.service.OrderFileRecordService;
import org.springframework.stereotype.Service;

@Service
public class OrderFileRecordServiceImpl extends SuperServiceImpl<OrderFileRecordMapper,OrderFileRecord> implements OrderFileRecordService {
}
