package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum  LightColor implements IEnum {

    WL(1,"白光"),
    NL(2,"暖光")
    ;
    LightColor(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(int code) {
        for (LightColor enums : LightColor.values()) {

            if (enums.code == code) {
                return enums.name;
            }

        }

        return "/";
    }

    public static LightColor getByCode(int code) {
        for (LightColor enums : LightColor.values()) {

            if (enums.code == code) {
                return enums;
            }

        }

        return null;
    }


    public int code;

    public String name;

    @Override
    public Serializable getValue() {
        return this.code;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }
}
