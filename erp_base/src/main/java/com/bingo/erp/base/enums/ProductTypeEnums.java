package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum ProductTypeEnums implements IEnum {

    Complete(1,"成品"),

    NotComplete(2,"半成品");


    private final int code;
    private final String name;

    ProductTypeEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ProductTypeEnums getByCode(int code){

        for (ProductTypeEnums enums:ProductTypeEnums.values()) {

            if(enums.code == code){
                return enums;
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
