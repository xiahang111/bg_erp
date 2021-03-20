package com.bingo.erp.commons.feign;

import com.bingo.erp.base.vo.CustomerVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "erp-person")
public interface PersonFeignClient {

    @RequestMapping(value = "/api-person/customer/saveCustomer",method = RequestMethod.POST)
    String saveCustomerByOrder(CustomerVO customerVO);

    @RequestMapping(value = "/api-person/customer/getCustomerNickByUid",method = RequestMethod.GET)
    String getCustomerNickByUid(@RequestParam("customerUid") String  customerUid);


}
