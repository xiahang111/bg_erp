package com.bingo.erp.xo.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.vo.StoreRecordInfo;
import com.bingo.erp.xo.order.vo.StatementResultVO;
import com.bingo.erp.xo.order.vo.StoreRecordPageVO;
import com.bingo.erp.xo.order.vo.StoreRecordVO;
import com.bingo.erp.xo.order.vo.StoreStatementVO;

import java.util.List;
import java.util.Map;


public interface StoreRecordInfoService extends SuperService<StoreRecordInfo> {

    IPage<StoreRecordInfo> getStoreRecord(StoreRecordPageVO storeRecordPageVO);

    void saveStoreRecord(StoreRecordVO storeRecordVO) throws  Exception;

    void callbackStoreRecord(String storeRecordUid) throws Exception;

    List<Map<String ,String >> getStoreNameList();

    List<StatementResultVO> getStoreStatement(StoreStatementVO storeStatementVO) throws MessageException;


}
