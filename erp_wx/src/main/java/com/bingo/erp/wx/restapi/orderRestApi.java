package com.bingo.erp.wx.restapi;

import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.wx.global.SysConf;
import com.bingo.erp.xo.wx.feign.OrderFeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api-wx/order")
public class orderRestApi {

    @Resource
    private OrderFeignClient orderFeignClient;

    @PostMapping("getByUser")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getOrderByAmindUid(HttpServletRequest request) {

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        try {
            return orderFeignClient.getOrderByAdminUid(adminUid);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.result(SysConf.Fail, e.getMessage());
        }

    }
}
