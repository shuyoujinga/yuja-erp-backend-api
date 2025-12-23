package org.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class MathUtils {

    private static final MathContext MC = new MathContext(16, RoundingMode.HALF_UP);
    private static final int DEFAULT_SCALE = 6;

    public static BigDecimal toBD(Object value) {
        if (value == null) return BigDecimal.ZERO;
        if (value instanceof BigDecimal) return (BigDecimal) value;
        if (value instanceof Number) return new BigDecimal(value.toString());
        return new BigDecimal(String.valueOf(value));
    }

    public static BigDecimal add(Object a, Object b) {
        return toBD(a).add(toBD(b), MC);
    }

    public static BigDecimal subtract(Object a, Object b) {
        return toBD(a).subtract(toBD(b), MC);
    }

    public static BigDecimal multiply(Object a, Object b) {
        return toBD(a).multiply(toBD(b), MC);
    }

    public static BigDecimal divide(Object a, Object b) {
        BigDecimal divisor = toBD(b);
        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("除数不能为0");
        }
        return toBD(a).divide(divisor, DEFAULT_SCALE, RoundingMode.HALF_UP);
    }

    public static boolean lt(Object a, Object b) {
        return toBD(a).compareTo(toBD(b)) < 0;
    }

    public static boolean gte(Object a, Object b) {
        return toBD(a).compareTo(toBD(b)) >= 0;
    }
}
