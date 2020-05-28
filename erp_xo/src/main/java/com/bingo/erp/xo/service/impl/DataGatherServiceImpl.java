package com.bingo.erp.xo.service.impl;

import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.DataGather;
import com.bingo.erp.xo.mapper.DataGatherMapper;
import com.bingo.erp.xo.service.DataGatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class DataGatherServiceImpl extends SuperServiceImpl<DataGatherMapper, DataGather> implements DataGatherService {

    @Resource
    private DataGatherMapper dataGatherMapper;

    @Override
    public DataGather getLastDataGather() {
        return dataGatherMapper.getLastDataGather();
    }
}
