package com.bingo.erp.xo.order.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.StoreOriginalInfo;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

public interface StoreOriginalInfoMapper extends SuperMapper<StoreOriginalInfo> {

    @Select("")
    Map<String ,String > queryOriginInfoData();
}
