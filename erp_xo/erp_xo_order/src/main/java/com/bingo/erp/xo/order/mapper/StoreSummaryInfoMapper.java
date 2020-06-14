package com.bingo.erp.xo.order.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.StoreSummaryInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface StoreSummaryInfoMapper extends SuperMapper<StoreSummaryInfo> {

    @Select("SELECT * FROM s_summary_info WHERE material_name = #{materialName}")
    StoreSummaryInfo getSummaryInfoByName(@Param("materialName") String materialName);
}
