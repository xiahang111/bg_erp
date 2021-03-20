package com.bingo.erp.xo.data.service;

import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.base.vo.CustomerVO;
import com.bingo.erp.xo.data.dto.CustomerTopDTO;
import com.bingo.erp.xo.data.entity.CustomerOrder;

import java.util.List;

public interface CustomerOrderService extends SuperService<CustomerOrder> {

    void saveCustomerOrder(CustomerVO customerVO);

    List<CustomerTopDTO> getCustomerTop();
}
