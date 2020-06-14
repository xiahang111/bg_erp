package com.bingo.erp.xo.order.service.impl;

import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.MaterialInfo;
import com.bingo.erp.xo.order.mapper.MaterialInfoMapper;
import com.bingo.erp.xo.order.service.MaterialInfoService;
import org.springframework.stereotype.Service;

@Service
public class MaterialInfoServiceImpl extends
        SuperServiceImpl<MaterialInfoMapper, MaterialInfo> implements MaterialInfoService {
}
