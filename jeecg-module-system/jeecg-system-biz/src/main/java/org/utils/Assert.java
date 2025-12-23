package org.utils;

import org.jeecg.common.exception.JeecgBootException;
import org.springframework.stereotype.Component;

@Component
public class Assert {

    public static void isTrue(boolean expression, String message) {
        if (expression) {
            throw new JeecgBootException(message);
        }
    }

}
