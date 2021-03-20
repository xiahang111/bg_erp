package com.bingo.erp.commons.feign;

import com.bingo.erp.base.vo.CustomerVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "erp-data")
public interface DataFeignClient {

    @RequestMapping(value = "/api-data/store/getStoreNumReport",method = RequestMethod.POST)
    String getStoreNumReport();

    @RequestMapping(value = "/api-data/customer/getCustomerTop",method = RequestMethod.POST)
    String getCustomerTop();

    @RequestMapping(value = "/api-data/customer/saveCustomerOrder",method = RequestMethod.POST)
    String saveCustomerOrder(CustomerVO customerVO);
}
