package com.bingo.erp.web.restapi;

import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.web.global.SysConf;
import com.bingo.erp.xo.order.global.MessageConf;
import com.bingo.erp.xo.order.service.AdminService;
import com.bingo.erp.xo.order.vo.AdminVO;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api-web/system")
@Slf4j
public class SystemRestApi {

    @Autowired
    private AdminService adminService;

    @PostMapping("/changePwd")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String changePwd(HttpServletRequest request, @ApiParam(name = "oldPwd", value = "旧密码", required = false) @RequestParam(name = "oldPwd", required = false) String oldPwd,
                            @ApiParam(name = "newPwd", value = "新密码", required = false) @RequestParam(name = "newPwd", required = false) String newPwd) {

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.TOKEN_DISABLE);
        }

        try {
            adminService.changePwd(adminUid,oldPwd, newPwd);
            return ResultUtil.result(SysConf.SUCCESS,MessageConf.UPDATE_SUCCESS);
        }catch (Exception e){
            log.error("更新失败:原因"+e.getMessage());
            return ResultUtil.result(SysConf.Fail,e.getMessage());
        }

    }

    @GetMapping("/getMe")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getMe(HttpServletRequest request){

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.TOKEN_DISABLE);
        }

        try {
            return ResultUtil.result(SysConf.SUCCESS,adminService.getMe(adminUid));
        }catch (Exception e){
            log.error("更新失败:原因"+e.getMessage());
            return ResultUtil.result(SysConf.Fail,e.getMessage());
        }
    }

    @PostMapping("/editMe")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String editMe(HttpServletRequest request,@RequestBody AdminVO adminVO) {
        // 参数校验
        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.TOKEN_DISABLE);
        }

        try {
            adminService.editMe(adminVO);
            return ResultUtil.result(SysConf.SUCCESS,MessageConf.UPDATE_SUCCESS);
        }catch (Exception e){
            log.error("更新失败:原因"+e.getMessage());
            return ResultUtil.result(SysConf.Fail,e.getMessage());
        }

    }
}
