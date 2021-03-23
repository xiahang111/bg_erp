package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import lombok.Data;

/**
 * 销售人员分组情况
 */
@Data
@TableName("t_group_salesman")
public class GroupSalesmanInfo extends SuperEntity<GroupSalesmanInfo> {

    private String groupName;

    private String salesman;
}
