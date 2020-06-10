package com.bingo.erp.xo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.StoreSummaryInfo;
import com.bingo.erp.xo.vo.StoreSummaryPageVO;
import com.bingo.erp.xo.vo.StoreSummaryVO;

import java.util.List;

public interface StoreSummaryInfoService extends SuperService<StoreSummaryInfo> {

    IPage<StoreSummaryInfo> getStoreSummaryByPage(StoreSummaryPageVO storeSummaryPageVO);

    void saveStoreSummary(StoreSummaryVO storeSummaryVO) throws Exception;

    void deleteStoreSummary(StoreSummaryVO storeSummaryVO);
}
