package com.bingo.erp.xo.data.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.xo.data.entity.StoreData;
import com.bingo.erp.xo.data.vo.BarReportResultVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StoreDataMapper extends SuperMapper<StoreData> {

    @Select("select store_total_num as value,date(create_time) as name from d_store_data where store_type = 'summary' limit 30")
    List<BarReportResultVO> getStoreNumReport();

}
