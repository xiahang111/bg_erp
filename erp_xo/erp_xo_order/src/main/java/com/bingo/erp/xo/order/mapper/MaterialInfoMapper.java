package com.bingo.erp.xo.order.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.MaterialInfo;
import com.bingo.erp.commons.entity.vo.GlassInfoVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MaterialInfoMapper extends SuperMapper<MaterialInfo> {

    @Select("SELECT * FROM t_material_info WHERE order_info_uid = #{orderInfoUid}")
    List<MaterialInfo> getAllByOrderUid(@Param("orderInfoUid") String orderInfoUid);

    @Select("SELECT " +
            "b.order_id, a.glass_height,a.glass_width ,a.glass_color" +
            " FROM t_material_info a " +
            "LEFT JOIN t_order_info b ON a.order_info_uid = b.uid order by b.order_id")
    List<GlassInfoVO> getGlassInfo();
}
