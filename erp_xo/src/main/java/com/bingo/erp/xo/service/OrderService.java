package com.bingo.erp.xo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.Admin;
import com.bingo.erp.commons.entity.MaterialInfo;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.xo.vo.IndexOrderVO;
import com.bingo.erp.xo.vo.MaterialVO;
import com.bingo.erp.xo.vo.OrderRecordPageVO;

import java.util.List;

public interface OrderService extends SuperService<OrderInfo> {

    List<String> saveOrder(MaterialVO materialVO) throws Exception;

    List<IndexOrderVO> getIndexOrderInfo();

    IPage<OrderInfo> getMaterialVOByUser(Admin admin, OrderRecordPageVO orderRecordPageVO);

    MaterialVO getMaterialVOByUid(String uid);
}
