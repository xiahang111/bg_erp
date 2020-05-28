package com.bingo.erp.utils;

import java.math.BigDecimal;

/**
 * 玻璃材料计算方法
 */
public class GlassCalculateUtil {

    public static BigDecimal[] calculateGlass(BigDecimal resourceHigth,BigDecimal resourceWidth,BigDecimal higth,BigDecimal width){

        BigDecimal[] result = new BigDecimal[2];

        result[0] = resourceHigth.subtract(higth);
        result[1] = resourceWidth.subtract(width);

        return result;
    }

}
