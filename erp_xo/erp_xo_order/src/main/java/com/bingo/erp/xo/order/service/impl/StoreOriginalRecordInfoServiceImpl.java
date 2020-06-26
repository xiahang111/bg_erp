package com.bingo.erp.xo.order.service.impl;

import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.StoreOriginalRecordInfo;
import com.bingo.erp.xo.order.mapper.StoreOriginalRecordInfoMapper;
import com.bingo.erp.xo.order.service.StoreOriginalRecordInfoService;
import org.springframework.stereotype.Service;

@Service
public class StoreOriginalRecordInfoServiceImpl
        extends SuperServiceImpl<StoreOriginalRecordInfoMapper, StoreOriginalRecordInfo> implements StoreOriginalRecordInfoService {
}
