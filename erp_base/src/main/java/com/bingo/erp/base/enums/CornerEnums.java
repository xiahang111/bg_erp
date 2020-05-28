package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum  CornerEnums implements IEnum {
    RightAngle(1,"直角"),
    BevelAngle(2,"45度角");


    CornerEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int code;

    public String name;
    
    public static CornerEnums getEnumByCode(int code){

        for (CornerEnums enums : CornerEnums.values()) {

            if (enums.code == code){
                return enums;
            }

        }

        return null;
        
    }

    public static String getNameByCode(int code){

        for (CornerEnums enums : CornerEnums.values()) {

            if (enums.code == code){
                return enums.name;
            }

        }

        return "";
    }

    public static CornerEnums getEnumByName(String name){

        for (CornerEnums enums : CornerEnums.values()) {

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
