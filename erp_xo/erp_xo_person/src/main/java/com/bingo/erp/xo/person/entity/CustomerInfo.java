package com.bingo.erp.xo.person.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import com.bingo.erp.base.enums.CustomerResourceEnums;
import lombok.Data;

@Data
@TableName("p_customer_info")
public class CustomerInfo extends SuperEntity<CustomerInfo> {

    @TableField("admin_uid")
    private String adminUid;

    private CustomerResourceEnums customerResource;

    private String salesman;

    private String customerName;

    private String customerAddr;

    private String customerPhone;


}
