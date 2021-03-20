package com.bingo.erp.xo.order.service;

import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.base.vo.CustomerVO;
import com.bingo.erp.commons.entity.CustomerInfo;

public interface CustomerInfoService extends SuperService<CustomerInfo> {

    void saveCustomerByOrder(String adminUid,CustomerVO customerVO);
}
