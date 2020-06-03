package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum TransomTypeEnums implements IEnum {

    TDHL_47(1, "47天地横梁"),
    TDHL_55(1, "55天地横梁");
    public final int code;
    public final String name;

    TransomTypeEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static TransomTypeEnums getByCode(int code) {

        for (TransomTypeEnums enums : TransomTypeEnums.values()) {

            if (enums.code == code) {
                return enums;
            }

        }

        return null;

    }

    public static String getEnumByCode(int code) {
        for (TransomTypeEnums enums : TransomTypeEnums.values()) {

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

    public int getCode() {
        return this.code;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }

}
