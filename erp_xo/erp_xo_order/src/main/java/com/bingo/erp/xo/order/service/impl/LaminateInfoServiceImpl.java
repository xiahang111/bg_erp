package com.bingo.erp.xo.order.service.impl;

import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.LaminateInfo;
import com.bingo.erp.xo.order.mapper.LaminaterInfoMapper;
import com.bingo.erp.xo.order.service.LaminateInfoService;
import org.springframework.stereotype.Service;

@Service
public class LaminateInfoServiceImpl extends SuperServiceImpl<LaminaterInfoMapper,LaminateInfo> implements LaminateInfoService {
}
