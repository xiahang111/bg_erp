package com.bingo.erp.base.enums;

import java.math.BigDecimal;

public enum ProductCalculateEnums {
    H1(1001,new BigDecimal(7),new BigDecimal(12),"5厘玻璃"),
    H2(1002,new BigDecimal(43),new BigDecimal(43),"4厘玻璃"),
    H3(1003,new BigDecimal(36),new BigDecimal(36),"5厘玻璃"),
    H4(2001,new BigDecimal(5),new BigDecimal(5),"5厘精磨边"),
    H5(3001,new BigDecimal(11),new BigDecimal(91),"5厘玻璃"),
    H6(4001,new BigDecimal(5),new BigDecimal(5),"5厘玻璃"),
    H7(5001,new BigDecimal(5),new BigDecimal(5),"5厘玻璃"),
    H8(5002,new BigDecimal(5),new BigDecimal(5),"5厘玻璃"),
    H9(5003,new BigDecimal(5),new BigDecimal(5),"5厘玻璃"),
    H10(6001,new BigDecimal(6),new BigDecimal(6),"4厘玻璃"),
    H11(7001,new BigDecimal(6),new BigDecimal(6),"4厘玻璃"),
    H12(7002,new BigDecimal(7),new BigDecimal(7),"4厘玻璃"),
    H13(7003,new BigDecimal(6),new BigDecimal(6),"4厘玻璃"),
    H14(7004,new BigDecimal(5),new BigDecimal(5),"5厘玻璃"),
    H15(8001,new BigDecimal(8),new BigDecimal(8),"5厘玻璃"),
    H16(8002,new BigDecimal(22),new BigDecimal(22),"5厘玻璃"),
    H17(8003,new BigDecimal(8),new BigDecimal(8),"无玻璃"),;




    private final int code;
    private final BigDecimal higth;
    private final BigDecimal width;
    private final String glassType;

    ProductCalculateEnums(int code, BigDecimal higth, BigDecimal width,String glassType) {
        this.code = code;
        this.higth = higth;
        this.width = width;
        this.glassType = glassType;
    }

    public int getCode() {
        return code;
    }

    public String getGlassType() {
        return glassType;
    }

    public BigDecimal getHigth() {
        return higth;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public static ProductCalculateEnums getByCode(int code) {

        for (ProductCalculateEnums enums : ProductCalculateEnums.values()) {

            if (enums.code == code) {
                return enums;
            }

        }

        return null;

    }
}
