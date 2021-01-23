package com.bingo.erp.xo.wx.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "erp-web")
public interface OrderFeignClient {

    @RequestMapping(value = "/order/getOrderByAdminUid",method = RequestMethod.GET)
    String getOrderByAdminUid(@RequestParam("adminUid") String adminUid);
}
