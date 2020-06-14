package com.bingo.erp.xo.person.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import com.bingo.erp.base.enums.CompanyUseEnums;
import lombok.Data;

@Data
@TableName("p_company_info")
public class CompanyInfo extends SuperEntity<CompanyInfo> {

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 公司负责人
     */
    private String companyBoss;

    /**
     * 公司电话
     */
    private String companyPhone;

    /**
     * 公司地址
     */
    private String companyAddr;

    /**
     * 公司性质
     */
    private CompanyUseEnums companyUse;
}
