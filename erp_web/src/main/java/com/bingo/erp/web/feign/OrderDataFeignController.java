package com.bingo.erp.web.feign;

import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.web.global.SysConf;
import com.bingo.erp.xo.order.service.feign.OrderDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("order")
public class OrderDataFeignController {


    @Resource
    private OrderDataService orderDataService;

    @GetMapping("getOrderByAdminUid")
    public String getOrderByAdminUid(HttpServletRequest request, @RequestParam("adminUid")String adminUid){
        return ResultUtil.result(SysConf.SUCCESS, orderDataService.getOrderByAdminUid(adminUid));
    }
}
