package com.bingo.erp.xo.person.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.base.vo.CustomerVO;
import com.bingo.erp.xo.person.entity.CompanyInfo;
import com.bingo.erp.xo.person.entity.CustomerInfo;
import com.bingo.erp.xo.person.vo.CompanyPageVO;
import com.bingo.erp.xo.person.vo.CompanyVO;

import java.util.List;

public interface CompanyInfoService extends SuperService<CompanyInfo> {


    IPage<CompanyInfo> getCompanyInfo(CompanyPageVO companyPageVO);

    void saveCompanyInfo(CompanyVO companyVO) throws Exception;

}
