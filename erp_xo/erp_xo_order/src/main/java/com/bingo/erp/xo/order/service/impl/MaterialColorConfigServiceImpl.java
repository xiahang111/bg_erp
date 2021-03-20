package com.bingo.erp.xo.order.service.impl;

import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.MaterialColorConfig;
import com.bingo.erp.xo.order.mapper.MaterialColorConfigMapper;
import com.bingo.erp.xo.order.service.MaterialColorConfigService;
import org.springframework.stereotype.Service;

@Service
public class MaterialColorConfigServiceImpl extends
        SuperServiceImpl<MaterialColorConfigMapper, MaterialColorConfig> implements MaterialColorConfigService {
}