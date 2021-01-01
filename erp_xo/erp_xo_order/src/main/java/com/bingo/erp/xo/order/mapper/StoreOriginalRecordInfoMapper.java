package com.bingo.erp.xo.order.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.StoreOriginalRecordInfo;
import com.bingo.erp.xo.order.vo.StatementQueryVO;
import com.bingo.erp.xo.order.vo.StatementResultVO;

import java.util.List;

/**
 * 坯料库存数据查询
 */
public interface StoreOriginalRecordInfoMapper extends SuperMapper<StoreOriginalRecordInfo> {

    /**
     * 根据条件查询坯料库存数据
     * @param statementQueryVO
     * @return
     */
    List<StatementResultVO> queryStoreOriginalRecordStatement(StatementQueryVO statementQueryVO);

}
