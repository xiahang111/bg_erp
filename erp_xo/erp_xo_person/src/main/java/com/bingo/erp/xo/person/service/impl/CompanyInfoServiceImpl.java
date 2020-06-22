package com.bingo.erp.xo.person.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.erp.base.enums.CompanyUseEnums;
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
import com.bingo.erp.xo.person.vo.CompanyVO;
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


        QueryWrapper<CompanyInfo> queryWrapper = new QueryWrapper<>();

        queryWrapper.orderByDesc("create_time");
        Page<CompanyInfo> companyInfoPage = new Page<>();
        companyInfoPage.setCurrent(companyPageVO.getCurrentPage());
        companyInfoPage.setSize(companyPageVO.getPageSize());
        if (StringUtils.isNotBlank(companyPageVO.getKeyword())) {
            queryWrapper.like("company_name", companyPageVO.getKeyword());
        }
        IPage<CompanyInfo> companyInfoIPage = companyInfoService.page(companyInfoPage, queryWrapper);

        return companyInfoIPage;
    }

    @Override
    public void saveCompanyInfo(CompanyVO companyVO) throws Exception {

        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setCompanyName(companyVO.getCompanyName());
        companyInfo.setCompanyAddr(companyVO.getCompanyAddr());
        companyInfo.setCompanyBoss(companyVO.getCompanyBoss());
        companyInfo.setCompanyPhone(companyVO.getCompanyPhone());
        companyInfo.setCompanyUse(CompanyUseEnums.getByCode(companyVO.getCompanyUse()));

        companyInfoService.save(companyInfo);

    }
}
