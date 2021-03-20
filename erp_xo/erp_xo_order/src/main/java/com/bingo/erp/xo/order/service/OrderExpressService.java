package com.bingo.erp.xo.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.OrderExpress;
import com.bingo.erp.xo.order.vo.OrderExpressPageVO;
import com.bingo.erp.xo.order.vo.OrderExpressVO;

public interface OrderExpressService extends SuperService<OrderExpress> {

    IPage<OrderExpress> getOrderExpressPage(OrderExpressPageVO orderExpressPageVO);

    void saveOrUpdate(String adminUid,OrderExpressVO orderExpressVO) throws Exception;
}
