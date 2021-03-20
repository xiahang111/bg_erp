package com.bingo.erp.web.restapi;

import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.web.global.SysConf;
import com.bingo.erp.xo.order.service.BaseDataService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api-web/basedata")
@Api(value = "基础数据相关api")
@Slf4j
public class BaseDataRestApi {

    @Resource
    private BaseDataService baseDataService;

    @PostMapping("getAllOrderMaker")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getAllOrderMaker(HttpServletRequest request){

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        try {
            return ResultUtil.result(SysConf.SUCCESS,baseDataService.getAllOrderMaker());
        }catch (Exception e){
            return ResultUtil.result(SysConf.Fail,"获取失败!原因:"+e.getMessage());
        }

    }

    @PostMapping("getAllSaleMan")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getAllSaleMan(HttpServletRequest request){

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        try {
            return ResultUtil.result(SysConf.SUCCESS,baseDataService.getAllSaleMan());
        }catch (Exception e){
            return ResultUtil.result(SysConf.Fail,"获取失败!原因:"+e.getMessage());
        }

    }

    @PostMapping("getAllColor")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getAllColor(HttpServletRequest request){

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        try {
            return ResultUtil.result(SysConf.SUCCESS,baseDataService.getAllColor());
        }catch (Exception e){
            return ResultUtil.result(SysConf.Fail,"获取失败!原因:"+e.getMessage());
        }

    }

    @PostMapping("getAllOrderType")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getAllOrderType(HttpServletRequest request){

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        try {
            return ResultUtil.result(SysConf.SUCCESS,baseDataService.getAllOrderType());
        }catch (Exception e){
            return ResultUtil.result(SysConf.Fail,"获取失败!原因:"+e.getMessage());
        }

    }

    @PostMapping("getAllOrderStatus")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getAllOrderStatus(HttpServletRequest request){

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        try {
            return ResultUtil.result(SysConf.SUCCESS,baseDataService.getAllOrderStatus());
        }catch (Exception e){
            return ResultUtil.result(SysConf.Fail,"获取失败!原因:"+e.getMessage());
        }

    }

    @PostMapping("getAllExpressRecord")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getAllExpressRecord(HttpServletRequest request){

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        try {
            return ResultUtil.result(SysConf.SUCCESS,baseDataService.getAllExpressRecord());
        }catch (Exception e){
            return ResultUtil.result(SysConf.Fail,"获取失败!原因:"+e.getMessage());
        }

    }

}
