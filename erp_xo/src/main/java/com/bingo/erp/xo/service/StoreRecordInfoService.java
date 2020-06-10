package com.bingo.erp.xo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.vo.StoreRecordInfo;
import com.bingo.erp.xo.vo.StoreRecordPageVO;
import com.bingo.erp.xo.vo.StoreRecordVO;

public interface StoreRecordInfoService extends SuperService<StoreRecordInfo> {

    IPage<StoreRecordInfo> getStoreRecord(StoreRecordPageVO storeRecordPageVO);

    void saveStoreRecord(StoreRecordVO storeRecordVO) throws  Exception;
}
