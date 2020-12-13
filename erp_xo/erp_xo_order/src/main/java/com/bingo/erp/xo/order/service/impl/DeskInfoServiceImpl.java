package com.bingo.erp.xo.order.service.impl;

import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.DeskInfo;
import com.bingo.erp.xo.order.mapper.DeskInfoMapper;
import com.bingo.erp.xo.order.service.DeskInfoService;
import org.springframework.stereotype.Service;

@Service
public class DeskInfoServiceImpl extends SuperServiceImpl<DeskInfoMapper,DeskInfo> implements DeskInfoService {
}
