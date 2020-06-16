package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum OrderStatusEnums implements IEnum {

    STAY_CONFIRM(1, "待确认"),
    CONFIRM(2, "已确认"),
    MAKING(3, "制作中"),
    STAY_DELIVER(4, "待发货"),
    COMPLETE(6, "已完成");


    OrderStatusEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getEnumByCode(int code) {
        for (OrderStatusEnums enums : OrderStatusEnums.values()) {

            if (enums.code == code) {
                return enums.name;
            }

        }

        return null;
    }

    public static OrderStatusEnums getByCode(int code) {
        for (OrderStatusEnums enums : OrderStatusEnums.values()) {

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
