package com.bingo.erp.xo.order.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.MetalInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MetalInfoMapper extends SuperMapper<MetalInfo> {

    @Select("SELECT * FROM t_metal_info WHERE order_info_uid = #{orderInfoUid}")
    List<MetalInfo> getAllByOrderUid(@Param("orderInfoUid") String orderInfoUid);
}
