package com.bingo.erp.person.restapi;

import com.bingo.erp.base.vo.CustomerVO;
import com.bingo.erp.person.global.SysConf;
import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.xo.person.service.CustomerInfoService;
import com.bingo.erp.xo.person.vo.CustomerPageVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api-person/customer")
@Api(value = "客户信息相关api", tags = {"CustomerRestApi"})
@Slf4j
public class CustomerRestApi {

    @Resource
    private CustomerInfoService customerInfoService;

    @PostMapping("/getCustomer")
    public String getCustomerByAdminUid(HttpServletRequest request, @RequestBody CustomerPageVO customerPageVO) {

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);

        if (null == adminUid) {
            return ResultUtil.result(SysConf.Fail, "token过期！");
        }

        try {
            return ResultUtil.result(SysConf.SUCCESS, customerInfoService.getCustomerByAdminUid(adminUid, customerPageVO));
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, e.getMessage());
        }

    }

    @PostMapping("/saveCustomer")
    public String saveCustomerByOrder(HttpServletRequest request, @RequestBody CustomerVO customerVO) {


        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);

        if (null == adminUid && null == customerVO.getAdminUid()) {
            return ResultUtil.result(SysConf.Fail, "token过期！");
        }

        customerInfoService.saveCustomerByOrder(adminUid, customerVO);

        return ResultUtil.result(SysConf.SUCCESS, "保存成功");

    }

    @GetMapping("/searchCustomer")
    public String searchCustomer(HttpServletRequest request,@RequestParam(required = false,defaultValue = "") String key){

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);

        if (null == adminUid) {
            return ResultUtil.result(SysConf.Fail, "token过期！");
        }

        return ResultUtil.result(SysConf.SUCCESS, customerInfoService.searchCustomer(adminUid,key));

    }
}
