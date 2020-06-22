package com.bingo.erp.xo.person.vo;

import com.bingo.erp.base.enums.CompanyUseEnums;
import lombok.Data;

@Data
public class CompanyVO {

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
    private Integer companyUse;

}
