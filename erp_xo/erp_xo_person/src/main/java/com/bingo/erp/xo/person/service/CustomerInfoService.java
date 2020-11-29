package com.bingo.erp.xo.person.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.base.vo.CustomerVO;
import com.bingo.erp.xo.person.entity.CustomerInfo;
import com.bingo.erp.xo.person.vo.CustomerPageVO;

import java.util.List;

public interface CustomerInfoService extends SuperService<CustomerInfo> {

    IPage<CustomerInfo> getCustomerByAdminUid(String adminUid, CustomerPageVO customerPageVO) throws Exception;

    void saveCustomerByOrder(String adminUid,CustomerVO customerVO);

    List<CustomerInfo> searchCustomer(String uid, String key);
}
