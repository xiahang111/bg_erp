package com.bingo.erp.base.vo;

import lombok.Data;

import java.io.Serializable;

//向person微服务保存客户信息的vo
@Data
public class CustomerVO implements Serializable {

    private String adminUid;

    private String cunstomerName;

    private String customerAddr;

    private String cutomerPhone;

    private String salesman;

    private Integer customerResource;

}
