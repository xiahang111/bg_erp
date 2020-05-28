package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum HandleEnums implements IEnum {

    NotHandle(0, "无拉手"),
    Handle168(1, "168拉手"),
    Handle1100(2, "1100拉手"),
    HandleTT(3, "通体拉手"),
    HandleXZ(4, "50斜边镶钻拉手"),


    ;

    HandleEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int code;

    public String name;

    public static HandleEnums getEnumByName(String name) {

        for (HandleEnums enums : HandleEnums.values()) {

            if (enums.name.equals(name)) {
                return enums;
            }

        }

        return null;

    }

    public static HandleEnums getByCode(int code) {

        for (HandleEnums enums : HandleEnums.values()) {

            if (enums.code == code) {
                return enums;
            }

        }

        return null;

    }

    public static String getNameByCode(int code) {
        for (HandleEnums enums : HandleEnums.values()) {

            if (enums.code == code) {
                return enums.name;
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
