package com.bingo.erp.web.restapi;

import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.web.global.SysConf;
import com.bingo.erp.xo.service.*;
import com.bingo.erp.xo.vo.GlassCalculateVO;
import com.bingo.erp.xo.vo.IndexOrderVO;
import com.bingo.erp.xo.vo.MaterialCalculateVO;
import com.bingo.erp.xo.vo.ProductRecordPageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/index")
@Api(value = "首页相关api")
@Slf4j
public class IndexRestApi {

    @Resource
    private DataGatherService dataGatherService;

    @Resource
    private OrderService orderService;

    @PostMapping("/getIndexData")
    @ApiOperation(value = "获取主页今日数据")
    @CrossOrigin(allowCredentials="true",allowedHeaders="*")
    public String getIndexData(){

        return ResultUtil.result(SysConf.SUCCESS,dataGatherService.getLastDataGather());
    }

    @GetMapping("getIndexOrderInfo")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getIndexOrderInfo(HttpServletResponse response, HttpServletRequest request){

        return ResultUtil.result(SysConf.SUCCESS,orderService.getIndexOrderInfo());

    }



}
