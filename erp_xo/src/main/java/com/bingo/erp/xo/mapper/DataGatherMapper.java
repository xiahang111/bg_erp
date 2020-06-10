package com.bingo.erp.xo.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.DataGather;
import org.apache.ibatis.annotations.Select;

public interface DataGatherMapper extends SuperMapper<DataGather> {

    @Select("SELECT * FROM t_data_gather order by create_time desc limit 1")
    DataGather getLastDataGather();


}
