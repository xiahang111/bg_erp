package com.bingo.erp.xo.service;

import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.xo.vo.MaterialVO;

import java.util.List;

public interface OrderService extends SuperService<OrderInfo> {

    List<String> saveOrder(MaterialVO materialVO) throws Exception;
}
