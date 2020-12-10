package com.bingo.erp.xo.person.feign;

import com.bingo.erp.base.vo.CustomerVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "erp-data")
public interface DataFeignClient {

    @RequestMapping(value = "/api-data/customer/saveCustomerOrder",method = RequestMethod.POST)
    String saveCustomerOrder(CustomerVO customerVO);
}
