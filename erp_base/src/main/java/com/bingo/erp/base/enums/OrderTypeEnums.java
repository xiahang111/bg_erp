package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum OrderTypeEnums implements IEnum {

    DOORORDER(1, "门单"),
    CBDORDER(2, "层板灯单"),
    DESK(3,"轻奢极简餐桌"),
    SHELF(4,"博古架"),
    CABINET(5,"玻璃柜"),
    SPECIMEN(6,"小样品"),
    HANGING(7,"置物架"),
    TRANSOM(8,"天地横梁"),
    METAL(9,"材料单");

    public final int code;
    public final String name;

    OrderTypeEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static OrderTypeEnums getByCode(int code) {

        for (OrderTypeEnums enums : OrderTypeEnums.values()) {

            if (enums.code == code) {
                return enums;
            }

        }

        return null;

    }

    public static String getEnumByCode(int code) {
        for (OrderTypeEnums enums : OrderTypeEnums.values()) {

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
