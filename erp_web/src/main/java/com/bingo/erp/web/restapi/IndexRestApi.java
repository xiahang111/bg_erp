package com.bingo.erp.web.restapi;

import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.web.global.SysConf;
import com.bingo.erp.xo.service.DataGatherService;
import com.bingo.erp.xo.service.MaterialCalculateRecordService;
import com.bingo.erp.xo.service.ProductCalculateRecordService;
import com.bingo.erp.xo.service.ProductService;
import com.bingo.erp.xo.vo.GlassCalculateVO;
import com.bingo.erp.xo.vo.MaterialCalculateVO;
import com.bingo.erp.xo.vo.ProductRecordPageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/index")
@Api(value = "首页相关api")
@Slf4j
public class IndexRestApi {

    @Resource
    private DataGatherService dataGatherService;

    @PostMapping("/getIndexData")
    @ApiOperation(value = "获取主页今日数据")
    @CrossOrigin(allowCredentials="true",allowedHeaders="*")
    public String getIndexData(){

        return ResultUtil.result(SysConf.SUCCESS,dataGatherService.getLastDataGather());
    }



}
