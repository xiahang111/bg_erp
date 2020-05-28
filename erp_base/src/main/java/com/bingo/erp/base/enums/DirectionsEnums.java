package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum  DirectionsEnums implements IEnum {
    FaceOpen(1,"对开"),
    LeftOpen(2,"左开"),
    RightOpen(3,"右开"),
    UpOpen(4,"上翻"),
    DownOpen(5,"下翻");


    DirectionsEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getEnumByCode(int code) {
        for (DirectionsEnums enums : DirectionsEnums.values()) {

            if (enums.code == code) {
                return enums.name;
            }

        }

        return null;
    }

    public static DirectionsEnums getByCode(int code) {
        for (DirectionsEnums enums : DirectionsEnums.values()) {

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
