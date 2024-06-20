package com.lxp.tool.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalHelper {
    private static final int DEF_DIV_SCALE = 10;

    private BigDecimalHelper() {
        throw new RuntimeException("BigDecimalHelper can't be initialized");
    }

    public static double add(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.add(b2).doubleValue();
    }

    public static double sub(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.subtract(b2).doubleValue();
    }

    public static double mul(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.multiply(b2).doubleValue();
    }

    public static double div(double value1, double value2) throws IllegalAccessException {
        return div(value1, value2, DEF_DIV_SCALE);
    }

    public static double div(double value1, double value2, int scale) throws IllegalAccessException {
        if (scale < 0) {
            throw new IllegalAccessException("scale can't be less than 0");
        }
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
    }

    public static boolean equals(BigDecimal num1, BigDecimal num2) {
        return num1.equals(num2);
    }

    public static int compare(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2);
    }
}
