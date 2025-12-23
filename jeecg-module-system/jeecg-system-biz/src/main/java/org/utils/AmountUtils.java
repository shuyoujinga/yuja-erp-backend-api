package org.utils;

import java.util.List;
import java.util.Optional;
import java.util.function.ToDoubleFunction;

public class AmountUtils {

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
}
