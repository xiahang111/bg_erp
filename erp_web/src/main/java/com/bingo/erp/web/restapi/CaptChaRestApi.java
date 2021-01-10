package com.bingo.erp.web.restapi;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api-web/captcha")
@Slf4j
public class CaptChaRestApi {

    @Autowired
    private CaptchaService captchaService;

    @ApiOperation(value = "验证码获取", notes = "验证码获取", response = String.class)
    @PostMapping(value = "/get")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public ResponseModel info(HttpServletRequest request, @RequestBody CaptchaVO captchaVO) {

        return captchaService.get(captchaVO);
    }

    @PostMapping("/check")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public ResponseModel check(@RequestBody CaptchaVO captchaVO) {
        return captchaService.check(captchaVO);
    }

    @PostMapping("/verify")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public ResponseModel verify(@RequestBody CaptchaVO captchaVO) {
        return captchaService.verification(captchaVO);
    }


}
