package com.bingo.erp.xo.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.StoreOriginalInfo;
import com.bingo.erp.commons.entity.StoreOriginalRecordInfo;
import com.bingo.erp.xo.order.vo.*;

import java.util.List;
import java.util.Map;

public interface StoreOriginalInfoService extends SuperService<StoreOriginalInfo> {

    IPage<StoreOriginalInfo> getStoreOriginal(StoreOriginalPageVO storeOriginalPageVO);

    IPage<StoreOriginalRecordInfo> getStoreOriginalRecord(StoreRecordPageVO storeRecordPageVO);

    void saveStoreOriginalRecord(StoreOriginRecordVO storeOriginRecordVO)throws Exception;

    void saveStoreOrigin(StoreOriginVO storeOriginVO) throws Exception;

    void deleteStoreOrigin(StoreOriginVO storeOriginVO);

    List<Map<String ,String >> getOriginNameList();
}
