package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum  CustomerResourceEnums implements IEnum {

    ORDER(1,"订单中保存客户"),
    ADDWEB(2,"前台保存")
    ;
    CustomerResourceEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(int code) {
        for (CustomerResourceEnums enums : CustomerResourceEnums.values()) {

            if (enums.code == code) {
                return enums.name;
            }

        }

        return null;
    }

    public static CustomerResourceEnums getByCode(int code) {
        for (CustomerResourceEnums enums : CustomerResourceEnums.values()) {

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
