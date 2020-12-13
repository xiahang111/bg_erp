package com.bingo.erp.base.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.Serializable;

public enum MaterialTypeEnums implements IEnum {
    LD1H(1001, "联动一号"),
    LD2H(1002, "联动二号"),
    LD3H(1003, "联动三号"),
    LD5H(1005,"联动五号"),
    SP2H(2001, "上品二号"),
    XB50(3001, "50斜边"),
    SP1H(4001, "上品一号"),
    ZB20(5001, "20窄边"),
    ZB22(5002, "22窄边"),
    JH22(5003, "22加厚"),
    TD1H(6001, "天地一号"),
    BG1H(7001, " 兵歌一号"),
    BG2H(7002, "兵歌二号"),
    BG3H(7003, "兵歌三号"),
    BG4H(7004, "兵歌四号"),
    CBD2(8001, "LED二代玻璃层板"),
    CBDJJ(8003, "LED酒格层板"),
    CBD1(8002, "一代层板灯");


    MaterialTypeEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getEnumByCode(int code) {
        for (MaterialTypeEnums enums : MaterialTypeEnums.values()) {

            if (enums.code == code) {
                return enums.name;
            }

        }

        return null;
    }

    public static MaterialTypeEnums getByCode(int code) {
        for (MaterialTypeEnums enums : MaterialTypeEnums.values()) {

            if (enums.code == code) {
                return enums;
            }

        }

        return null;
    }

    public static int getCodeByInformation(String information){

        for (MaterialTypeEnums enums:MaterialTypeEnums.values()) {
            if (information.contains(enums.name)){
                return enums.code;
            }
        }

        return 0;
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
