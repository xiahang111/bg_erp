package com.bingo.erp.xo.data.service;

import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.xo.data.entity.StoreData;
import com.bingo.erp.xo.data.vo.BarReportResultVO;

import java.util.List;

public interface StoreDataService extends SuperService<StoreData> {

    /**
     * 获取最近一条库存数据
     * @return
     */
    List<StoreData> getLastStoreDatas();

    /**
     * 获取
     * @return
     */
    List<BarReportResultVO> getStoreNumReport();

}
