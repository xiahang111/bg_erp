package com.bingo.erp.xo.order.enums;

import com.bingo.erp.xo.order.factory.*;

public enum MaterialFactoryEnum {


    LD1H(1001, LD1HCalculateFactory.class),
    LD2H(1002, LD2HCalculateFactory.class),
    LD3H(1003, LD3HCalculateFactory.class),
    XB50(3001, XB50CalculateFactory.class),
    ZB20(5001, ZB20CalculateFactory.class),
    ZB22(5002, ZB22CalculateFactory.class),
    JH22(5003, JH22CalculateFactory.class),
    TD1H(6001, TD1HCalculateFactory.class),
    SP1H(4001, SP1HCalculateFactory.class),
    SP2H(2001, SP2HCalculateFactory.class),
    BG4H(7004, BG4HCalculateFactory.class),
    BG1H(7001, NotCalculateFactory.class),
    BG2H(7002, NotCalculateFactory.class),
    BG3H(7003, NotCalculateFactory.class),
    CBD(8001, CBDCalculateFactory.class),
    CBD1(8002, CBDCalculateFactory.class),
    JGCB(8003, CBDCalculateFactory.class),;


    MaterialFactoryEnum(int code, Class factoryClass) {
        this.code = code;
        this.factoryClass = factoryClass;
    }

    private int code;
    private Class factoryClass;

    public static Class getFactoryClass(int code) {

        for (MaterialFactoryEnum enums : MaterialFactoryEnum.values()) {

            if (enums.code == code) {
                return enums.factoryClass;
            }

        }

        return null;

    }


}
