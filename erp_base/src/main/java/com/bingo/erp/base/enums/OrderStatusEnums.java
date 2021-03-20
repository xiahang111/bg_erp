package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum OrderStatusEnums implements IEnum {

    STAY_CONFIRM(1, "待确认", ""),
    CONFIRM(2, "已确认", ""),
    MAKING1(3, "制作中", "已下车间"),
    MAKING2(3, "制作中", "进行中"),
    Package1(4, "包装完成", "已包"),
    Package2(5, "包装完成", "已打包"),
    STAY_DELIVER(6, "待发货","可发"),
    STAY_DELIVER1(7, "待发货","可发货"),
    DeliverNotFinish(8, "未发完","未发完"),
    COMPLETE(9, "已完成", "完");


    OrderStatusEnums(int code, String name,String label) {
        this.code = code;
        this.name = name;
        this.label = label;
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

    public static OrderStatusEnums getByLabel(String label){
        for (OrderStatusEnums enums : OrderStatusEnums.values()) {

            if (enums.label.equals(label)) {
                return enums;
            }
        }
        return OrderStatusEnums.CONFIRM;
    }

    public final int code;
    public final String name;
    public String label;

    @Override
    public Serializable getValue() {
        return this.code;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }
}
