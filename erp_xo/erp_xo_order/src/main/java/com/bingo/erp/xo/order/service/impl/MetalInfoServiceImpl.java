package com.bingo.erp.xo.order.service.impl;

import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.MetalInfo;
import com.bingo.erp.xo.order.mapper.MetalInfoMapper;
import com.bingo.erp.xo.order.service.MetalInfoService;
import org.springframework.stereotype.Service;

@Service
public class MetalInfoServiceImpl extends SuperServiceImpl<MetalInfoMapper,MetalInfo> implements MetalInfoService {
}
