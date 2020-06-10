package com.bingo.erp.xo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.Admin;
import com.bingo.erp.commons.entity.MaterialInfo;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.xo.vo.*;

import java.util.List;

public interface OrderService extends SuperService<OrderInfo> {

    List<String> saveOrder(MaterialVO materialVO) throws Exception;

    List<String> saveCBDOrder(LaminateVO laminateVO) throws Exception;

    List<IndexOrderVO> getIndexOrderInfo();

    IPage<OrderInfo> getMaterialVOByUser(Admin admin, OrderRecordPageVO orderRecordPageVO);

    ProductVO getMaterialVOByUid(String uid);
}
