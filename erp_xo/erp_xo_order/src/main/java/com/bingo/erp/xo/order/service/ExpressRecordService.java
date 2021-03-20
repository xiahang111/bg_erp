package com.bingo.erp.xo.order.service;

import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.ExpressRecord;

import java.util.List;

public interface ExpressRecordService extends SuperService<ExpressRecord> {

    List<String> getAllExpressRecord();

}
