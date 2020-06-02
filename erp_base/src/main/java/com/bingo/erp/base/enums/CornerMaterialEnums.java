package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

/**
 * 角码枚举类
 */
public enum  CornerMaterialEnums  implements IEnum {


    None(0,"无角码",0,0),
    XHJ20(1,"20锌合金角码",4,4),
    LX20(2,"20铝芯角码",4,2),
    XHJ22(3,"22锌合金角码",4,4),
    LX22(4,"22铝芯角码",4,2),
    SPJM(5,"上品角码",4,4),
    LD1HXHJM(6,"联动1号锌合金角码",4,4),
    LD2HTPJM(7,"联动2号铁片角码",4,4),
    LD3HTPJM(8,"联动3号铁片角码",4,4)
    ;


    public int code;

    public String name;

    public int cornerNum;

    public int screwNum;


    CornerMaterialEnums(int code, String name, int cornerNum, int screwNum) {
        this.code = code;
        this.name = name;
        this.cornerNum = cornerNum;
        this.screwNum = screwNum;
    }


    public static String getEnumByCode(int code) {
        for (CornerMaterialEnums enums : CornerMaterialEnums.values()) {

            if (enums.code == code) {
                return enums.name;
            }

        }

        return null;
    }
    public static CornerMaterialEnums getByCode(int code) {

        for (CornerMaterialEnums enums : CornerMaterialEnums.values()) {

            if (enums.code == code) {
                return enums;
            }

        }

        return null;

    }

    @Override
    public Serializable getValue() {
        return this.code;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }
}
