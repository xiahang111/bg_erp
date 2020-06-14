package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum CompanyUseEnums implements IEnum {

    LAC(1,"拉丝厂"),
    YHC(2,"氧化厂"),
    BLC(3,"玻璃厂");
    CompanyUseEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(int code) {
        for (CompanyUseEnums enums : CompanyUseEnums.values()) {

            if (enums.code == code) {
                return enums.name;
            }

        }

        return null;
    }

    public static CompanyUseEnums getByCode(int code) {
        for (CompanyUseEnums enums : CompanyUseEnums.values()) {

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
