package com.aaa.commondevelop.util;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;


/**
 * @author liuzhen.tian
 * @version 1.0 Stringutil.java  2022/10/23 14:23
 */
public class StrUtil {

    /**
     * 指定分割
     *
     * @param packagePatterns 待分割字符
     * @param delimiters      分割符号 ",;"（根据任意,;分割）
     * @return
     */
    public static String[] tokenizeToStringArray(String packagePatterns, String delimiters) {
        String configLocationDelimiters = ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS;
        return StringUtils.tokenizeToStringArray(packagePatterns, StringUtils.isEmpty(delimiters) ? configLocationDelimiters : delimiters);
    }

    public static void main(String[] args) {
        String[] x = tokenizeToStringArray("xxx?xxx", "?");
        System.out.println(x);
    }
}
