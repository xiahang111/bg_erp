package com.bingo.erp.xo.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.MaterialInfo;
import com.bingo.erp.commons.entity.OrderInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MaterialInfoMapper extends SuperMapper<MaterialInfo> {

    @Select("SELECT * FROM t_material_info WHERE order_info_uid = #{orderInfoUid}")
    List<MaterialInfo> getAllByOrderUid(@Param("orderInfoUid")String orderInfoUid);
}
