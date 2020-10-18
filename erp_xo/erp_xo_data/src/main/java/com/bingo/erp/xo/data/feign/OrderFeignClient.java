package com.bingo.erp.xo.data.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@FeignClient(name = "erp-web")
public interface OrderFeignClient {

    @RequestMapping(value = "/feign/storeData",method = RequestMethod.GET)
    String getStoreData();
}
