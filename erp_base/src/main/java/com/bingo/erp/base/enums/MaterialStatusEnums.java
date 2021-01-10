package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum  MaterialStatusEnums implements IEnum {

    STOREIN(1,"入库"),
    LASI(2,"拉丝"),
    XIXING(3,"铣型"),
    YANGHUA(4,"氧化"),
    FINISHED(5,"已完成"),
    PENTU(6,"喷涂"),
    PAOGUANG(7,"抛光"),
    BAOFEI(8,"报废"),
    WAIXIAO(9,"外销或发车间");



    MaterialStatusEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getEnumByCode(int code) {
        for (MaterialStatusEnums enums : MaterialStatusEnums.values()) {

            if (enums.code == code) {
                return enums.name;
            }

        }

        return null;
    }

    public static MaterialStatusEnums getByCode(int code) {
        for (MaterialStatusEnums enums : MaterialStatusEnums.values()) {

            if (enums.code == code) {
                return enums;
            }

        }

        return null;
    }

    public static MaterialStatusEnums getByName(String  name) {
        for (MaterialStatusEnums enums : MaterialStatusEnums.values()) {

            if (enums.name.equals(name)) {
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
