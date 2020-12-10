package com.bingo.erp.xo.order.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.StoreOriginalRecordInfo;
import com.bingo.erp.xo.order.vo.StatementQueryVO;
import com.bingo.erp.xo.order.vo.StatementResultVO;

import java.util.List;

public interface StoreOriginalRecordInfoMapper extends SuperMapper<StoreOriginalRecordInfo> {

    List<StatementResultVO> queryStoreOriginalRecordStatement(StatementQueryVO statementQueryVO);

}
