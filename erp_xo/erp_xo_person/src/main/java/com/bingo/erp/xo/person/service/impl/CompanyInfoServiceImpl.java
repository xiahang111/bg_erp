package com.bingo.erp.xo.person.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.erp.base.enums.CustomerResourceEnums;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.global.BaseRedisConf;
import com.bingo.erp.base.global.BaseSysConf;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.base.vo.CustomerVO;
import com.bingo.erp.commons.entity.Admin;
import com.bingo.erp.utils.JsonUtils;
import com.bingo.erp.utils.RedisUtil;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.person.entity.CompanyInfo;
import com.bingo.erp.xo.person.entity.CustomerInfo;
import com.bingo.erp.xo.person.mapper.CompanyInfoMapper;
import com.bingo.erp.xo.person.mapper.CustomerInfoMapper;
import com.bingo.erp.xo.person.service.CompanyInfoService;
import com.bingo.erp.xo.person.service.CustomerInfoService;
import com.bingo.erp.xo.person.vo.CompanyPageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompanyInfoServiceImpl extends SuperServiceImpl<CompanyInfoMapper, CompanyInfo> implements CompanyInfoService {


    @Resource
    private CompanyInfoService companyInfoService;

    @Override
    public IPage<CompanyInfo> getCompanyInfo(CompanyPageVO companyPageVO) {

        Page<CompanyInfo> companyInfoPage = new Page<>();
        companyInfoPage.setCurrent(companyPageVO.getCurrentPage());
        companyInfoPage.setSize(companyPageVO.getPageSize());
        IPage<CompanyInfo> companyInfoIPage = companyInfoService.page(companyInfoPage);

        return companyInfoIPage;
    }
}
