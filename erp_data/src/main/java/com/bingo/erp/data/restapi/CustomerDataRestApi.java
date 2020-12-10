package com.bingo.erp.data.restapi;

import com.bingo.erp.base.global.BaseSysConf;
import com.bingo.erp.base.vo.CustomerVO;
import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.xo.data.service.CustomerOrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api-data/customer")
public class CustomerDataRestApi {


    @Autowired
    private CustomerOrderService customerOrderService;

    @PostMapping("/saveCustomerOrder")
    @ApiOperation(value = "保存用户订单数据")
    @CrossOrigin(allowCredentials="true",allowedHeaders="*")
    public String saveCustomerOrder(HttpServletRequest request, @RequestBody CustomerVO customerVO){
        try {
            customerOrderService.saveCustomerOrder(customerVO);
            return ResultUtil.result(BaseSysConf.SUCCESS,"");
        }catch (Exception e){
            return ResultUtil.result(BaseSysConf.Fail,e.getMessage());
        }

    }
}
