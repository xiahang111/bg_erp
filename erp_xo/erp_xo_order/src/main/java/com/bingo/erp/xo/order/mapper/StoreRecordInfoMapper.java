package com.bingo.erp.xo.order.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.vo.StoreRecordInfo;
import com.bingo.erp.xo.order.vo.StatementQueryVO;
import com.bingo.erp.xo.order.vo.StatementResultVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreRecordInfoMapper extends SuperMapper<StoreRecordInfo> {

    List<StatementResultVO> queryStoreRecordStatement(StatementQueryVO statementQueryVO);
}
