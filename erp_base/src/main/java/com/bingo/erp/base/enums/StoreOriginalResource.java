package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum  StoreOriginalResource implements IEnum {

    STORE_IN_XX(1,"铣型料入库"),
    STORE_IN_RY(2,"荣一料入库"),
    STORE_IN_SD(3,"山东料进库"),
    STORE_OUT_CJ(4,"出车间"),
    STORE_OUT_KD(5,"出康达氧化"),
    STORE_OUT_DM(6,"出东美氧化"),
    STORE_OUT_FH(7,"出风和氧化"),
    STORE_OUT_XX(8,"出铣型厂"),
    STORE_OUT_WX(9,"外销发货"),
    STORE_OUT_SL(10,"出三联喷涂厂"),
    STORE_OUT_JS(14,"出金色恒辉喷涂厂"),
    STORE_OUT_YM(11,"出原美喷涂厂"),
    STORE_OUT_YH(12,"出亿和氧化厂"),
    STORE_OUT_CY(13,"出长远拉丝厂");


    StoreOriginalResource(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(int code) {
        for (StoreOriginalResource enums : StoreOriginalResource.values()) {

            if (enums.code == code) {
                return enums.name;
            }

        }

        return null;
    }

    public static StoreOriginalResource getByCode(int code) {
        for (StoreOriginalResource enums : StoreOriginalResource.values()) {

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
