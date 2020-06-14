package com.bingo.erp.xo.person.service;

import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.base.vo.CustomerVO;
import com.bingo.erp.xo.person.entity.CustomerInfo;

import java.util.List;

public interface CustomerInfoService extends SuperService<CustomerInfo> {

    List<CustomerInfo> getCustomerByAdminUid(String adminUid) throws Exception;

    void saveCustomerByOrder(CustomerVO customerVO);
}
