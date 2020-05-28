package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum  LightEnums implements IEnum {

    Light(1,"带灯"),
    NotLight(2,"不带灯");

     LightEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int code;

    public String name;

    public static LightEnums getEnumByName(String name){

        for (LightEnums enums : LightEnums.values()) {

            if (enums.name.equals(name)){
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
    public String getName(){
        return this.name;
    }

}
