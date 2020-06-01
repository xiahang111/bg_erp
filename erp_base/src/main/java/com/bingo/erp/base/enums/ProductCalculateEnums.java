package com.bingo.erp.base.enums;

import java.math.BigDecimal;

public enum ProductCalculateEnums {
    H1(1001,new BigDecimal(7),new BigDecimal(12)),
    H2(1002,new BigDecimal(43),new BigDecimal(43)),
    H3(1003,new BigDecimal(36),new BigDecimal(36)),
    H4(2001,new BigDecimal(5),new BigDecimal(5)),
    H5(3001,new BigDecimal(92),new BigDecimal(92)),
    H6(4001,new BigDecimal(5),new BigDecimal(5)),
    H7(5001,new BigDecimal(5),new BigDecimal(5)),
    H8(5002,new BigDecimal(5),new BigDecimal(5)),
    H9(5003,new BigDecimal(5),new BigDecimal(5)),
    H10(6001,new BigDecimal(6),new BigDecimal(6)),
    H11(7001,new BigDecimal(6),new BigDecimal(6)),
    H12(7002,new BigDecimal(7),new BigDecimal(7)),
    H13(7003,new BigDecimal(6),new BigDecimal(6)),
    H14(7004,new BigDecimal(5),new BigDecimal(5)),
    H15(8001,new BigDecimal(8),new BigDecimal(8));




    private final int code;
    private final BigDecimal higth;
    private final BigDecimal width;

    ProductCalculateEnums(int code, BigDecimal higth, BigDecimal width) {
        this.code = code;
        this.higth = higth;
        this.width = width;
    }

    public int getCode() {
        return code;
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
