package com.bingo.erp.xo.order.service.impl;

import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.ExpressRecord;
import com.bingo.erp.xo.order.mapper.ExpressRecordMapper;
import com.bingo.erp.xo.order.service.ExpressRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExpressRecordServiceImpl extends SuperServiceImpl<ExpressRecordMapper, ExpressRecord> implements ExpressRecordService {


    @Resource
    private ExpressRecordService expressRecordService;

    @Override
    public List<String> getAllExpressRecord() {
        return expressRecordService.getAllExpressRecord();
    }
}
