package com.bingo.erp.web.feign;

import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.web.global.SysConf;
import com.bingo.erp.xo.order.service.feign.StoreDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("feign")
public class StoreDataFeignController {

    @Autowired
    private StoreDataService storeDataService;

    @GetMapping("storeData")
    public String getStoreData(HttpServletRequest request){

        return ResultUtil.result(SysConf.SUCCESS,storeDataService.getStoreData());
    }
}
