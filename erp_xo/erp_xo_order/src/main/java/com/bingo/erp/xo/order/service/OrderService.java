package com.bingo.erp.xo.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.Admin;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.xo.order.vo.*;

import java.util.List;

public interface OrderService extends SuperService<OrderInfo> {

    List<String> saveOrder(String adminUid, MaterialVO materialVO) throws Exception;

    List<String> saveCBDOrder(String adminUid, LaminateVO laminateVO) throws Exception;

    List<IndexOrderVO> getIndexOrderInfo();

    IPage<OrderInfo> getMaterialVOByUser(Admin admin, OrderRecordPageVO orderRecordPageVO);

    ProductVO getMaterialVOByUid(String uid);
}
