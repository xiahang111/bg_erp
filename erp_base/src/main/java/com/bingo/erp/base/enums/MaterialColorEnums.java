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
    LSHUI(8, "拉丝灰"),
    OGH(9, "欧歌红"),
    CYH(10, "瓷泳黑"),
    LSJ(11, "拉丝金"),
    HS(12, "黑色"),
    JS(13, "金色"),
    SJS(14, "深金色"),
    GTS(15, "古铜色");

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

        return "/";
    }

    public static MaterialColorEnums getByCode(int code) {
        for (MaterialColorEnums enums : MaterialColorEnums.values()) {

            if (enums.code == code) {
                return enums;
            }

        }

        return null;
    }

    public static MaterialColorEnums getByName(String name) {
        for (MaterialColorEnums enums : MaterialColorEnums.values()) {

            if (enums.name.equals(name)) {
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
