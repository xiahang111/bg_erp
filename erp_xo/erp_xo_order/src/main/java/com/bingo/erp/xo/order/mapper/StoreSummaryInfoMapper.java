package com.bingo.erp.xo.order.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.StoreSummaryInfo;
import com.bingo.erp.xo.order.vo.BarReportResultVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

public interface StoreSummaryInfoMapper extends SuperMapper<StoreSummaryInfo> {

    @Select("SELECT * FROM s_summary_info WHERE material_name = #{materialName}")
    StoreSummaryInfo getSummaryInfoByName(@Param("materialName") String materialName);

    @Select("SELECT material_name from s_summary_info where 1=1")
    Set<String > getStoreNameList();

    @Select("select material_name as name , total_weight as value from s_summary_info order by total_weight desc LIMIT 15")
    List<BarReportResultVO> getAllStoreWeightTop();

    @Select("select material_name as name, total_price as value from s_summary_info order by total_price desc LIMIT 12")
    List<BarReportResultVO> getAllStorePriceTop();


}
