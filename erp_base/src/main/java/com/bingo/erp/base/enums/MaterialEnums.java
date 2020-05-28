package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum MaterialEnums implements IEnum {
    widthMaterial(1,"横料"),
    heightMaterial(2,"竖料"),
    heightLightMaterial(3,"带灯竖料"),
    heightHandleMaterial(4,"带拉手竖料");


    MaterialEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int code;

    public String name;

    public static MaterialEnums getEnumsByName(String name){

        for (MaterialEnums enums: MaterialEnums.values()) {

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
