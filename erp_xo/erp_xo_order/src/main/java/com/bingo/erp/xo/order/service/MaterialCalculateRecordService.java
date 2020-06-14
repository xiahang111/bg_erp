package com.bingo.erp.xo.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.MaterialCalculateRecord;
import com.bingo.erp.xo.order.vo.ProductRecordPageVO;


public interface MaterialCalculateRecordService extends SuperService<MaterialCalculateRecord> {

    IPage<MaterialCalculateRecord> getMaterialRecordPage(ProductRecordPageVO productRecordPageVO);
}
