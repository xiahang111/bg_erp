package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum LineColor implements IEnum {

    None(0, "无"),
    WHITELINE(1, "白色"),
    BLACELINE(2, "黑色");

    LineColor(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(int code) {
        for (LineColor enums : LineColor.values()) {

            if (enums.code == code) {
                return enums.name;
            }

        }

        return "/";
    }

    public static LineColor getByCode(int code) {
        for (LineColor enums : LineColor.values()) {

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
