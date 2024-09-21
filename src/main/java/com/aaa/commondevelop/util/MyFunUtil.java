package com.aaa.commondevelop.util;

import com.aaa.commondevelop.service.common.MyFun;
import tk.mybatis.mapper.weekend.reflection.ReflectionOperationException;

import java.beans.Introspector;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author liuzhen.tian
 * @version 1.0 FunUtil.java  2023/7/30 18:10
 */
public class MyFunUtil {
    private static final Pattern GET_PATTERN = Pattern.compile("^get[A-Z].*");
    private static final Pattern IS_PATTERN = Pattern.compile("^is[A-Z].*");

    private MyFunUtil() {
    }

    public static <T, R> String getFieldName(MyFun<T, R> fn) {
        try {
            Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(fn);
            String getter = serializedLambda.getImplMethodName();
            if (GET_PATTERN.matcher(getter).matches()) {
                getter = getter.substring(3);
            } else if (IS_PATTERN.matcher(getter).matches()) {
                getter = getter.substring(2);
            }
            return Introspector.decapitalize(getter);
        } catch (ReflectiveOperationException e) {
            throw new ReflectionOperationException(e);
        }
    }
}
