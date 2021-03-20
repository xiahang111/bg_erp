package com.bingo.erp.xo.order.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.vo.StoreRecordInfo;
import com.bingo.erp.xo.order.vo.DoubleBarReportReslutVO;
import com.bingo.erp.xo.order.vo.StatementQueryVO;
import com.bingo.erp.xo.order.vo.StatementResultVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StoreRecordInfoMapper extends SuperMapper<StoreRecordInfo> {

    List<StatementResultVO> queryStoreRecordStatement(StatementQueryVO statementQueryVO);

    @Select("select SUM(material_num) as value1 ,date(create_time) as name ,material_status as value2 from s_record_info GROUP BY material_status,date(create_time) ORDER BY date(create_time) limit 20")
    List<DoubleBarReportReslutVO> getDoubleBarReport();
}
