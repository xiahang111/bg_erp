package com.bingo.erp.xo.order.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.HangingInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface HangingInfoMapper extends SuperMapper<HangingInfo> {

    @Select("SELECT * FROM t_hanging_info WHERE order_info_uid = #{orderInfoUid}")
    List<HangingInfo> getAllByOrderUid(@Param("orderInfoUid") String orderInfoUid);
}
