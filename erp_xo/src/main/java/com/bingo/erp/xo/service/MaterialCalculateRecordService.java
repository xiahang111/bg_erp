package com.bingo.erp.xo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.MaterialCalculateRecord;
import com.bingo.erp.xo.vo.ProductRecordPageVO;

public interface MaterialCalculateRecordService extends SuperService<MaterialCalculateRecord> {

    IPage<MaterialCalculateRecord> getMaterialRecordPage(ProductRecordPageVO productRecordPageVO);
}
