package com.bingo.erp.data.restapi;

import com.bingo.erp.base.global.BaseSysConf;
import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.xo.data.service.StoreDataService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api-data/store")
public class StoreDataRestApi {


    @Autowired
    private StoreDataService storeDataService;

    @PostMapping("/getTotalData")
    @ApiOperation(value = "获取主页今日数据")
    @CrossOrigin(allowCredentials="true",allowedHeaders="*")
    public String getIndexStoreData(HttpServletRequest request){

        return ResultUtil.result(BaseSysConf.SUCCESS,storeDataService.getLastStoreDatas());

    }

    @PostMapping("/getStoreNumReport")
    @ApiOperation(value = "获取主页今日数据")
    @CrossOrigin(allowCredentials="true",allowedHeaders="*")
    public String getStoreNumReport(HttpServletRequest request){

        return ResultUtil.result(BaseSysConf.SUCCESS,storeDataService.getStoreNumReport());

    }

}
