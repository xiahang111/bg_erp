package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum GlassColor implements IEnum {
    NOGLASS(0,"无"),
    OZH(1,"欧洲灰"),
    LXH(2,"蓝星灰"),
    JC(3,"金茶"),
    QC(4,"浅茶"),
    HH(5,"长虹"),
    BB(6,"白玻"),
    HB(7,"黑玻"),
    CYLS(8,"超白春意阑珊"),
    SYHB(9,"丝印黑边"),
    YJ(10,"银镜"),
    CBCH(11,"超白长虹");


    GlassColor(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(int code) {
        for (GlassColor enums : GlassColor.values()) {

            if (enums.code == code) {
                return enums.name;
            }

        }

        return null;
    }

    public static GlassColor getByCode(int code) {
        for (GlassColor enums : GlassColor.values()) {

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
