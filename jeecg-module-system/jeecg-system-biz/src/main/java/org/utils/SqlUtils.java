package org.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlUtils {

    /**
     * 替换 SQL 中 ${key} 占位符
     * @param sql 原 SQL
     * @param paramMap key-value 映射
     * @return 替换后的 SQL
     */
    public static String replacePlaceholders(String sql, Map<String, String> paramMap) {
        if (sql == null || paramMap == null) return sql;

        Pattern pattern = Pattern.compile("\\$\\{(\\w+)\\}");
        Matcher matcher = pattern.matcher(sql);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = paramMap.getOrDefault(key, "");  // 没有值则替换为空
            // 加单引号，保证 SQL 字符串安全
            value = "'" + value + "'";
            matcher.appendReplacement(sb, value);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 将 SQL 中所有 ${xxx} 占位符替换为空字符串 ''
     * @param sql 原 SQL
     * @return 替换后的 SQL
     */
    public static String replaceAllPlaceholders(String sql) {
        if (sql == null) return null;
        // 正则匹配 ${xxx}
        return sql.replaceAll("\\$\\{[^}]+\\}", "''");
    }
}
