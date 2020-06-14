package com.bingo.erp.person.restapi;

import com.bingo.erp.person.global.SysConf;
import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.xo.person.service.CompanyInfoService;
import com.bingo.erp.xo.person.vo.CompanyPageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api-person/company")
@Slf4j
public class CompanyRestApi {


    @Resource
    private CompanyInfoService companyInfoService;

    @PostMapping("/getCompany")
    public String getCompany(HttpServletRequest request, @RequestBody CompanyPageVO companyPageVO) {

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);

        if (null == adminUid) {
            return ResultUtil.result(SysConf.Fail, "token过期！");
        }

        return ResultUtil.result(SysConf.SUCCESS, companyInfoService.getCompanyInfo(companyPageVO));
    }
}
