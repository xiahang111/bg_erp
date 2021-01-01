package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;

/**
 * 在外材料信息表
 */
@TableName("s_store_outside_info")
public class StoreOutsideInfo extends SuperEntity<StoreOutsideInfo> {


    private String materialName;

    private String specification;

    private String unit;
}
