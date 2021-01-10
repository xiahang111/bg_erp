package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import com.bingo.erp.base.enums.MaterialColorEnums;
import lombok.Data;

@TableName("c_material_remind_config")
@Data
public class MaterialRemindConfig extends SuperEntity<MaterialRemindConfig> {


    /**
     * 材料名称
     */
    private String materialName;

    /**
     * 规格
     */
    private String specification;

    /**
     * 材料颜色
     */
    private MaterialColorEnums materialColor;

    /**
     * 告警阈值
     */
    private Integer threshold;

    /**
     * 告警方式 1、弹窗 2、邮件 3、短信
     */
    private Integer remindMethod;



}
