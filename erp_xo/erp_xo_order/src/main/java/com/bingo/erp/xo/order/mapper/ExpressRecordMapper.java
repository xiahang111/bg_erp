package com.bingo.erp.xo.order.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.ExpressRecord;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ExpressRecordMapper extends SuperMapper<ExpressRecord> {

    @Select("select express_name from t_express_record group by express_name")
    List<String> getAllExpress();
}
