package com.bingo.erp.xo.service.impl;

import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.MaterialInfo;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.xo.mapper.MaterialInfoMapper;
import com.bingo.erp.xo.mapper.OrderInfoMapper;
import com.bingo.erp.xo.service.MaterialInfoService;
import org.springframework.stereotype.Service;

@Service
public class MaterialInfoServiceImpl extends
        SuperServiceImpl<MaterialInfoMapper, MaterialInfo> implements MaterialInfoService {
}
