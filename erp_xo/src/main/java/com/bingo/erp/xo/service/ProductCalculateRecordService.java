package com.bingo.erp.xo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.ProductCalculateRecord;
import com.bingo.erp.xo.vo.GlassCalculateRecordVO;
import com.bingo.erp.xo.vo.ProductRecordPageVO;

import java.util.List;

public interface ProductCalculateRecordService extends SuperService<ProductCalculateRecord> {

    List<GlassCalculateRecordVO> getAllGlassCalculateRecord();

    IPage<ProductCalculateRecord> getGlassRecordPage(ProductRecordPageVO productRecordPageVO);

}
