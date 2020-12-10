package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum  IronwareColorEnums implements IEnum {
    NOCOLOR(0,"无"),
    BLACK(1,"黑色"),
    GRAY(2,"灰色"),
    GOLD(3,"金色");

    public int code;

    public String name;

    IronwareColorEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getEnumByCode(int code) {
        for (IronwareColorEnums enums : IronwareColorEnums.values()) {

            if (enums.code == code) {
                return enums.name;
            }

        }

        return "/";
    }

    public static IronwareColorEnums getEnumByName(String name) {
        for (IronwareColorEnums enums : IronwareColorEnums.values()) {

            if (enums.name.equals(name)) {
                return enums;
            }

        }

        return IronwareColorEnums.NOCOLOR;
    }


    @Override
    public Serializable getValue() {
        return this.code;
    }

    @JsonValue
    public String getName(){
        return this.name;
    }
}
