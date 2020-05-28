package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum MaterialColorEnums implements IEnum {
    HTLS(1, "黄铜拉丝"),
    TLS(2, "古铜拉丝"),
    YH(3, "哑黑"),
    CSH(4, "瓷砂黑"),
    LMH(5, "罗马灰"),
    SSH(6, "绅士灰"),
    LSH(7, "拉丝黑"),
    LSHUI(8, "拉丝灰");

    MaterialColorEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getEnumByCode(int code) {
        for (MaterialColorEnums enums : MaterialColorEnums.values()) {

            if (enums.code == code) {
                return enums.name;
            }

        }

        return null;
    }

    public static MaterialColorEnums getByCode(int code) {
        for (MaterialColorEnums enums : MaterialColorEnums.values()) {

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
