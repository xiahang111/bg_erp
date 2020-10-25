package com.bingo.erp.xo.order.service.impl;

import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.IronwareInfo;
import com.bingo.erp.xo.order.mapper.IronwareInfoMapper;
import com.bingo.erp.xo.order.service.IronwareInforService;
import org.springframework.stereotype.Service;

@Service
public class IronwareInfoServiceImpl extends SuperServiceImpl<IronwareInfoMapper,IronwareInfo> implements IronwareInforService {
}
