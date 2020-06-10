package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum StoreMaterialStatus implements IEnum {

    IN(1,"入库"),
    OUT(2,"出库")
    ;

    StoreMaterialStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(int code) {
        for (StoreMaterialStatus enums : StoreMaterialStatus.values()) {

            if (enums.code == code) {
                return enums.name;
            }

        }

        return null;
    }

    public static StoreMaterialStatus getByCode(int code) {
        for (StoreMaterialStatus enums : StoreMaterialStatus.values()) {

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
