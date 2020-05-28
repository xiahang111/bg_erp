package com.bingo.erp.base.enums;

import java.math.BigDecimal;

public enum  MaterialCalculateEnums {
    //LD1H(1001,"联动1号",MaterialEnums.heightMaterial);
    ;

    MaterialCalculateEnums(int code,
                           String name,
                           MaterialEnums heightMaterial,
                           Integer heightMaterialNum,
                           BigDecimal heightCalculate,
                           MaterialEnums widthMaterial,
                           Integer widthMaterialNum,
                           BigDecimal widthCalculate,
                           CornerEnums corner,
                           LightEnums light) {
        this.code = code;
        this.name = name;
        this.heightMaterial = heightMaterial;
        this.heightMaterialNum = heightMaterialNum;
        this.heightCalculate = heightCalculate;
        this.widthMaterial = widthMaterial;
        this.widthMaterialNum = widthMaterialNum;
        this.widthCalculate = widthCalculate;
        this.corner = corner;
        this.light = light;
    }

    private int code;
    private String name;
    private MaterialEnums heightMaterial;
    private Integer heightMaterialNum;
    private BigDecimal heightCalculate;
    private MaterialEnums widthMaterial;
    private Integer widthMaterialNum;
    private BigDecimal widthCalculate;
    private CornerEnums corner;
    private LightEnums light;

}
