package com.bingo.erp.xo.order.service.impl;

import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.StoreOriginalInfo;
import com.bingo.erp.xo.order.mapper.StoreOriginalInfoMapper;
import com.bingo.erp.xo.order.service.StoreOriginalInfoService;
import org.springframework.stereotype.Service;

@Service
public class StoreOriginalInfoServiceImpl
        extends SuperServiceImpl<StoreOriginalInfoMapper, StoreOriginalInfo> implements StoreOriginalInfoService {
}
