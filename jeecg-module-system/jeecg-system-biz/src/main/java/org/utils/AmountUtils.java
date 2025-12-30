package org.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

public class AmountUtils {
    /**
     * 金额取反（符号翻转）
     *
     * @param amount 原金额
     * @return 取反后的金额
     */
    public static double negate(double amount) {
        return -amount;
    }
    private static final int SCALE = 2; // 金额统一 2 位小数
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;
    /**
     * 累加明细金额，返回总金额
     *
     * @param details 明细列表
     * @param amountGetter 获取明细金额的方法
     * @param <T> 明细类型
     * @return 总金额
     */
    public static <T> double sumTotalAmount(List<T> details, ToDoubleFunction<T> amountGetter) {
        if (details == null || details.isEmpty()) {
            return 0d;
        }

        double total = 0d;
        for (T item : details) {
            total += Optional.ofNullable(amountGetter.applyAsDouble(item)).orElse(0d);
        }
        return total;
    }


    /* ================= 加 / 减 ================= */

    public static double add(double a, double b) {
        return toBigDecimal(a)
                .add(toBigDecimal(b))
                .setScale(SCALE, ROUNDING)
                .doubleValue();
    }

    public static double subtract(double a, double b) {
        return toBigDecimal(a)
                .subtract(toBigDecimal(b))
                .setScale(SCALE, ROUNDING)
                .doubleValue();
    }

    public static double add(double... amounts) {
        BigDecimal result = BigDecimal.ZERO;
        if (amounts != null) {
            for (double amount : amounts) {
                result = result.add(toBigDecimal(amount));
            }
        }
        return result.setScale(SCALE, ROUNDING).doubleValue();
    }

    public static double subtract(double base, double... amounts) {
        BigDecimal result = toBigDecimal(base);
        if (amounts != null) {
            for (double amount : amounts) {
                result = result.subtract(toBigDecimal(amount));
            }
        }
        return result.setScale(SCALE, ROUNDING).doubleValue();
    }

    /* ================= 汇总 ================= */

    public static <T> double sum(
            List<T> details,
            Function<T, BigDecimal> amountGetter) {

        if (details == null || details.isEmpty()) {
            return 0d;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (T item : details) {
            BigDecimal amount = amountGetter.apply(item);
            total = total.add(defaultZero(amount));
        }

        return total.setScale(SCALE, ROUNDING).doubleValue();
    }

    /* ================= 内部工具 ================= */

    private static BigDecimal toBigDecimal(double value) {
        // 关键点：禁止 new BigDecimal(double)
        return BigDecimal.valueOf(value);
    }

    private static BigDecimal defaultZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    public static boolean isZero(Double stockQty) {
        return stockQty == null || stockQty.compareTo(0D) == 0;
    }

}
