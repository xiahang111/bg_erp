package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum StoreMaterialResource implements IEnum {

    STORE_IN_CD(1, "康达料入库"),
    STORE_IN_DM(2, "东美料入库"),
    STORE_IN_FH(3, "风和料入库"),
    STORE_IN_CQ(4, "超强料入库"),
    STORE_OUT_CJ(5, "车间用料"),
    STORE_OUT_WX(6, "外销发货"),
    STORE_IN_SL(7,"三联喷涂厂入库"),
    STORE_IN_YM(8,"原美喷涂厂入库"),
    STORE_IN_YH(9,"亿和氧化厂入库");

    StoreMaterialResource(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(int code) {
        for (StoreMaterialResource enums : StoreMaterialResource.values()) {

            if (enums.code == code) {
                return enums.name;
            }

        }

        return null;
    }

    public static StoreMaterialResource getByCode(int code) {
        for (StoreMaterialResource enums : StoreMaterialResource.values()) {

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
