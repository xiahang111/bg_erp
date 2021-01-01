package com.bingo.erp.base.enums;

import java.math.BigDecimal;

public enum ProductCalculateEnums {
    H1(1001,new BigDecimal(7),new BigDecimal(12),"5mm钢化"),
    H2(1002,new BigDecimal(43),new BigDecimal(43),"4mm玻璃"),
    H3(1003,new BigDecimal(36),new BigDecimal(36),"5mm钢化"),
    H4(2001,new BigDecimal(5),new BigDecimal(5),"5mm钢化"),
    H5(3001,new BigDecimal(11),new BigDecimal(91),"5mm钢化"),
    H6(4001,new BigDecimal(5),new BigDecimal(5),"5mm钢化"),
    H7(5001,new BigDecimal(5),new BigDecimal(5),"5mm钢化"),
    H8(5002,new BigDecimal(5),new BigDecimal(5),"5mm钢化"),
    H9(5003,new BigDecimal(5),new BigDecimal(5),"5mm钢化"),
    H10(6001,new BigDecimal(6),new BigDecimal(6),"4厘玻璃"),
    H11(7001,new BigDecimal(6),new BigDecimal(6),"4厘玻璃"),
    H12(7002,new BigDecimal(7),new BigDecimal(7),"4厘玻璃"),
    H13(7003,new BigDecimal(6),new BigDecimal(6),"4厘玻璃"),
    H14(7004,new BigDecimal(5),new BigDecimal(5),"5mm钢化"),
    H15(8001,new BigDecimal(8),new BigDecimal(8),"5mm钢化"),
    H16(8002,new BigDecimal(22),new BigDecimal(22),"5mm钢化"),
    H17(8003,new BigDecimal(8),new BigDecimal(8),"无玻璃"),
    H18(1005,new BigDecimal(4),new BigDecimal(20),"5mm钢化"),
    H19(1004,new BigDecimal(0),new BigDecimal(25),"5mm精磨边");

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
