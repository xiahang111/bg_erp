package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum MaterialTypeEnums implements IEnum {
    LD1H(1001, "联动1号"),
    LD2H(1002, "联动2号"),
    LD3H(1003, "联动3号"),
    LD5H(1005,"联动5号"),
    SP2H(2001, "上品2号"),
    XB50(3001, "50斜边"),
    SP1H(4001, "上品1号"),
    ZB20(5001, "20窄边"),
    ZB22(5002, "22窄边"),
    JH22(5003, "22加厚"),
    TD1H(1001, "天地1号"),
    BG1H(7001, "兵哥1号"),
    BG2H(7002, "兵哥2号"),
    BG3H(7003, "兵哥3号"),
    BG4H(7004, "兵哥4号"),
    CBD2(8001, "LED二代玻璃层板"),
    CBDJJ(8003, "LED酒格层板"),
    CBD1(8002, "一代层板灯");


    MaterialTypeEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getEnumByCode(int code) {
        for (MaterialTypeEnums enums : MaterialTypeEnums.values()) {

            if (enums.code == code) {
                return enums.name;
            }

        }

        return null;
    }

    public static MaterialTypeEnums getByCode(int code) {
        for (MaterialTypeEnums enums : MaterialTypeEnums.values()) {

            if (enums.code == code) {
                return enums;
            }

        }

        return null;
    }

    public final int code;
    public final String name;

    @Override
    public Serializable getValue() {
        return this.code;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }

}
