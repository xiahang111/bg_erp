package com.bingo.erp.xo.order.service.impl;

import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.HangingInfo;
import com.bingo.erp.xo.order.mapper.HangingInfoMapper;
import com.bingo.erp.xo.order.service.HangingInfoService;
import org.springframework.stereotype.Service;

@Service
public class HangingInfoServiceImpl extends SuperServiceImpl<HangingInfoMapper,HangingInfo> implements HangingInfoService {
}
