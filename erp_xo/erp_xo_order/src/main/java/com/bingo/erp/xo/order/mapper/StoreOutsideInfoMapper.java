package com.bingo.erp.xo.order.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.StoreOutsideInfo;
import com.bingo.erp.xo.order.vo.StoreOutsidePageVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface StoreOutsideInfoMapper extends SuperMapper<StoreOutsideInfo> {


    @Select("select * from s_store_outside_info where origin_uid = #{originUid}")
    StoreOutsideInfo getByOriginUid(@Param("originUid")String originUid);

    IPage<StoreOutsideInfo> queryStoreOutside(Page<StoreOutsideInfo> page, @Param("pageVO") StoreOutsidePageVO pageVO);

}
