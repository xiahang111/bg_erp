package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import lombok.Data;

@Data
@TableName("c_material_color_config")
public class MaterialColorConfig extends SuperEntity<MaterialColorConfig> {

    private Integer colorCode;

    private String colorName;
}
