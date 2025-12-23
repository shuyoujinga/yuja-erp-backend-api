package org.utils;

import org.springframework.stereotype.Component;

@Component
public class CommonMethodUtils {

    // 方法：获取自增后的代码
    public static String incrementCode(String code) {
        // 正则表达式匹配字母部分和数字部分
        String letterPart = code.replaceAll("[0-9]", "");
        String numberPart = code.replaceAll("[^0-9]", "");

        // 将数字部分转为整数并自增
        int number = Integer.parseInt(numberPart);
        number++;

        // 保留原始数字部分的长度，补零
        String newNumberPart = String.format("%0" + numberPart.length() + "d", number);

        // 重新组合新的代码
        return letterPart + newNumberPart;
    }
    /**
     * 将编码从 A01A01 格式转换为 A0101 格式
     *
     * @param code 原始编码
     * @return 转换后的编码
     */
    public static String convertMaterialCode(String code) {
        return code.substring(0, 3) + code.substring(4, 6) ;
    }

}
