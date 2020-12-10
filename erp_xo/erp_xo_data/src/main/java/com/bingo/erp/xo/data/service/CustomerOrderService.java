package com.bingo.erp.xo.data.service;

import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.base.vo.CustomerVO;
import com.bingo.erp.xo.data.entity.CustomerOrder;

public interface CustomerOrderService extends SuperService<CustomerOrder> {

    void saveCustomerOrder(CustomerVO customerVO);
}
