package com.bingo.erp.xo.order.enums;

import com.bingo.erp.base.enums.CornerMaterialEnums;

public enum  MaterialCornerEnum {

    LD1H(1001, CornerMaterialEnums.LD1HXHJM),
    LD2H(1002, CornerMaterialEnums.LD2HTPJM),
    LD3H(1003, CornerMaterialEnums.LD3HTPJM),
    LD5H(1005, CornerMaterialEnums.LD1HXHJM),
    XB50(3001, CornerMaterialEnums.None),
    ZB20(5001, CornerMaterialEnums.XHJ20),
    ZB22(5002, CornerMaterialEnums.XHJ22),
    JH22(5003, CornerMaterialEnums.SPJM),
    TD1H(6001, CornerMaterialEnums.SPJM),
    SP1H(4001, CornerMaterialEnums.SPJM),
    SP2H(2001, CornerMaterialEnums.SPJM),
    BG4H(7004, CornerMaterialEnums.XHJ22),
    BG1H(7001, CornerMaterialEnums.SPJM),
    BG2H(7002, CornerMaterialEnums.SPJM),
    BG3H(7003, CornerMaterialEnums.SPJM);
    ;

    MaterialCornerEnum(int materialType, CornerMaterialEnums corner) {
        this.materialType = materialType;
        this.corner = corner;
    }

    private int materialType;

    private CornerMaterialEnums corner;

    public static CornerMaterialEnums getCornerByMaterialType(Integer materialType){

        for (MaterialCornerEnum cornerEnum:MaterialCornerEnum.values()) {
            if(cornerEnum.materialType == materialType){
                return cornerEnum.corner;
            }
        }

        return CornerMaterialEnums.None;

    }
}
