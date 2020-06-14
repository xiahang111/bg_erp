package com.bingo.erp.xo.order.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.IronwareInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IronwareInfoMapper extends SuperMapper<IronwareInfo> {

    @Select("SELECT * FROM t_ironware_info WHERE order_info_uid = #{orderInfoUid}")
    List<IronwareInfo> getAllByOrderUid(@Param("orderInfoUid") String orderInfoUid);
}
