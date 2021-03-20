package com.bingo.erp.web.restapi;

import com.bingo.erp.commons.entity.Admin;
import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.web.global.SysConf;
import com.bingo.erp.xo.order.service.AdminService;
import com.bingo.erp.xo.order.service.ReportService;
import com.bingo.erp.xo.order.vo.SalesmanMonthReportVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api-web/report")
@Api(value = "报表相关api")
@Slf4j
public class ReportRestApi {

    @Resource
    private AdminService adminService;

    @Resource
    private ReportService reportService;

    @PostMapping("ordermaker")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String orderMakerReport(HttpServletRequest request) {

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());

        if (!admin.getUserName().equals(SysConf.ADMIN)) {
            return ResultUtil.result(SysConf.ERROR, "权限不足!");
        }

        try {
            return ResultUtil.result(SysConf.SUCCESS, reportService.getOrderMakerData());
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, "获取失败!原因:" + e.getMessage());
        }
    }

    @PostMapping("ordermakersale")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String orderMakerSaleReport(HttpServletRequest request) {

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());

        if (!admin.getUserName().equals(SysConf.ADMIN)) {
            return ResultUtil.result(SysConf.ERROR, "权限不足!");
        }

        try {
            return ResultUtil.result(SysConf.SUCCESS, reportService.getOrderMakerSaleData());
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, "获取失败!原因:" + e.getMessage());
        }
    }

    @PostMapping("storeAllWeightTop")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String storeAllWeightTop(HttpServletRequest request) {

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());

        if (!admin.getUserName().equals(SysConf.ADMIN)) {
            return ResultUtil.result(SysConf.ERROR, "权限不足!");
        }

        try {
            return ResultUtil.result(SysConf.SUCCESS, reportService.getAllStoreWeightTop());
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, "获取失败!原因:" + e.getMessage());
        }
    }

    @PostMapping("storeAllPriceTop")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String storeAllPriceTop(HttpServletRequest request) {

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }


        try {
            return ResultUtil.result(SysConf.SUCCESS, reportService.getAllStorePriceTop());
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, "获取失败!原因:" + e.getMessage());
        }
    }

    @PostMapping("storeNumReport")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String storeNumReport(HttpServletRequest request) {

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());

        if (!admin.getUserName().equals(SysConf.ADMIN)) {
            return ResultUtil.result(SysConf.ERROR, "权限不足!");
        }

        try {
            return ResultUtil.result(SysConf.SUCCESS, reportService.getStoreNumReport());
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, "获取失败!原因:" + e.getMessage());
        }
    }

    @PostMapping("storeInOutReport")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String storeInOutReport(HttpServletRequest request) {

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());

        if (!admin.getUserName().equals(SysConf.ADMIN)) {
            return ResultUtil.result(SysConf.ERROR, "权限不足!");
        }

        try {
            return ResultUtil.result(SysConf.SUCCESS, reportService.getStoreInOutReport());
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, "获取失败!原因:" + e.getMessage());
        }
    }

    @PostMapping("salesmanNumReport")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String salesmanNumReport(HttpServletRequest request) {

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());

        if (!admin.getUserName().equals(SysConf.ADMIN)) {
            return ResultUtil.result(SysConf.ERROR, "权限不足!");
        }

        try {
            return ResultUtil.result(SysConf.SUCCESS, reportService.getSalesmanNumReport());
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, "获取失败!原因:" + e.getMessage());
        }
    }

    @PostMapping("salesmanPriceReport")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String salesmanPriceReport(HttpServletRequest request) {

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());

        if (!admin.getUserName().equals(SysConf.ADMIN)) {
            return ResultUtil.result(SysConf.ERROR, "权限不足!");
        }

        try {
            return ResultUtil.result(SysConf.SUCCESS, reportService.getSalesmanPriceReport());
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, "获取失败!原因:" + e.getMessage());
        }
    }

    @PostMapping("salesmanMonthReport")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String salesmanMonthReport(HttpServletRequest request, @RequestBody SalesmanMonthReportVO salesmanMonthReportVO) {

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());

        if (!admin.getUserName().equals(SysConf.ADMIN)) {
            return ResultUtil.result(SysConf.ERROR, "权限不足!");
        }

        try {
            return ResultUtil.result(SysConf.SUCCESS, reportService.getSalesmanMonthReportByName(salesmanMonthReportVO.getSalesmanName()));
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, "获取失败!原因:" + e.getMessage());
        }
    }

    @PostMapping("getOrderTypePie")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getOrderTypePie(HttpServletRequest request) {

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }


        try {
            return ResultUtil.result(SysConf.SUCCESS, reportService.getOrderTypePie());
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, "获取失败!原因:" + e.getMessage());
        }
    }

    @PostMapping("getDvOrderNum")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getDvOrderNum(HttpServletRequest request) {

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        try {
            return ResultUtil.result(SysConf.SUCCESS, reportService.getDvOrderNum());
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, "获取失败!原因:" + e.getMessage());
        }
    }

    @PostMapping("getDvOrderCenter")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getDvOrderCenter(HttpServletRequest request) {

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        try {
            return ResultUtil.result(SysConf.SUCCESS, reportService.getDvOrderCenter());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.result(SysConf.Fail, "获取失败!原因:" + e.getMessage());
        }
    }

    @PostMapping("getCustomerList")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getCustomerList(HttpServletRequest request) {

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        try {
            return ResultUtil.result(SysConf.SUCCESS, reportService.getCustomerList());
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, "获取失败!原因:" + e.getMessage());
        }
    }
}
