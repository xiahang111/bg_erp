package com.bingo.erp.web.restapi;

import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.web.global.SysConf;
import com.bingo.erp.xo.order.service.OrderExpressService;
import com.bingo.erp.xo.order.vo.OrderExpressPageVO;
import com.bingo.erp.xo.order.vo.OrderExpressVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api-web/order-express")
@Api(value = "订单流程相关api")
@Slf4j
public class OrderExpressRestApi {

    @Resource
    private OrderExpressService orderExpressService;

    @PostMapping("getOrderExpress")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getOrderExpress(HttpServletRequest request, @RequestBody OrderExpressPageVO orderExpressPageVO) {

        if (request.getAttribute(SysConf.ADMIN_UID) == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        try {
            return ResultUtil.result(SysConf.SUCCESS, orderExpressService.getOrderExpressPage(orderExpressPageVO));

        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, e.getMessage());

        }
    }

    @PostMapping("saveOrderExpress")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String saveOrderExpress(HttpServletRequest request, @RequestBody OrderExpressVO orderExpressVO) {

        if (request.getAttribute(SysConf.ADMIN_UID) == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();

        try {
            orderExpressService.saveOrUpdate(adminUid,orderExpressVO);
            return ResultUtil.result(SysConf.SUCCESS, "请求成功");
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, e.getMessage());
        }
    }

}










