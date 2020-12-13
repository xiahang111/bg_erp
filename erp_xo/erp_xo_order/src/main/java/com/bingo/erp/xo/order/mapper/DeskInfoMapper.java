package com.bingo.erp.xo.order.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.DeskInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DeskInfoMapper extends SuperMapper<DeskInfo> {

    @Select("SELECT * FROM t_desk_info WHERE order_info_uid = #{orderInfoUid}")
    List<DeskInfo> getAllByOrderUid(@Param("orderInfoUid") String orderInfoUid);
}
