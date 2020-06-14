package com.bingo.erp.xo.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.StoreSummaryInfo;
import com.bingo.erp.xo.order.vo.StoreSummaryPageVO;
import com.bingo.erp.xo.order.vo.StoreSummaryVO;


public interface StoreSummaryInfoService extends SuperService<StoreSummaryInfo> {

    IPage<StoreSummaryInfo> getStoreSummaryByPage(StoreSummaryPageVO storeSummaryPageVO);

    void saveStoreSummary(StoreSummaryVO storeSummaryVO) throws Exception;

    void deleteStoreSummary(StoreSummaryVO storeSummaryVO);
}
