package com.bingo.erp.web.restapi;

import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.web.global.SysConf;
import com.bingo.erp.xo.order.service.UserService;
import com.bingo.erp.xo.order.vo.AddUserVO;
import com.bingo.erp.xo.order.vo.UserPageVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api-web/user")
@Slf4j
public class UserRestApi {


    @Resource
    private UserService userService;

    /**
     * 获取所有用户信息，只有admin权限才可以
     *
     * @return
     */
    @ApiOperation(value = "获取所有用户信息", notes = "获取所有用户信息")
    @PostMapping("getAllUser")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getAllUser(HttpServletRequest request, @RequestBody UserPageVO userPageVO) {

        if (request.getAttribute(SysConf.ADMIN_UID) == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);

        try {
            return ResultUtil.result(SysConf.SUCCESS, userService.getAllUser(adminUid, userPageVO));
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, e.getMessage());
        }

    }

    /**
     * 获取所有用户信息，只有admin权限才可以
     *
     * @return
     */
    @ApiOperation(value = "获取所有权限信息", notes = "获取所有权限信息")
    @PostMapping("getAllRole")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getAllRole(HttpServletRequest request) {
        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }
        try {
            return ResultUtil.result(SysConf.SUCCESS, userService.getAllRole(adminUid));
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, e.getMessage());
        }

    }


    /**
     * 获取所有用户信息，只有admin权限才可以
     *
     * @return
     */
    @ApiOperation(value = "获取所有权限信息", notes = "获取所有权限信息")
    @PostMapping("saveUser")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String saveUser(HttpServletRequest request, @RequestBody AddUserVO addUserVO){


        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        try {
            userService.AddUser(adminUid,addUserVO);
            return ResultUtil.result(SysConf.SUCCESS,"添加用户成功！");
        }catch (Exception e){
            return ResultUtil.result(SysConf.Fail, e.getMessage());
        }

    }
}
