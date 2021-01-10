package com.bingo.erp.xo.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bingo.erp.commons.entity.StoreOriginalRecordInfo;
import com.bingo.erp.commons.entity.StoreOutsideInfo;
import com.bingo.erp.xo.order.vo.StoreOutsidePageVO;
import com.bingo.erp.xo.order.vo.StoreOutsideVO;

/**
 * 材料外出服务类
 */
public interface StoreOutsideService {

    void saveByOriginalRecord(StoreOriginalRecordInfo storeOriginalRecordInfo,Integer materialColor);

    void updateStoreOutsideByUid(StoreOutsideVO storeOutsideVO) throws Exception;

    IPage<StoreOutsideInfo> getList(StoreOutsidePageVO storeOutsidePageVO);

    void updateStoreOutsideData(StoreOutsideVO storeOutsideVO) throws Exception;


}
