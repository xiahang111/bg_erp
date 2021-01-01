package com.bingo.erp.xo.order.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.OrderGlassDetail;
import com.bingo.erp.xo.order.vo.GlassInfoPageVO;
import org.apache.ibatis.annotations.Param;

public interface OrderGlassDetailMapper extends SuperMapper<OrderGlassDetail> {

    IPage<OrderGlassDetail> getGlassDetailPage(Page<OrderGlassDetail> page, @Param("pageVO") GlassInfoPageVO glassInfoPageVO);
}
