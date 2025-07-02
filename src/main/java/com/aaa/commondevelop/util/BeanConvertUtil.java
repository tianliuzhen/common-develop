package com.aaa.commondevelop.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author liuzhen.tian
 * @version 1.0 BeanConvertUtil.java  2025/7/2 21:07
 */
public class BeanConvertUtil {
    public static <T> T listTo(Object obj, Class<T> tClass) {
        return JSONObject.parseObject(JSONObject.toJSONString(obj), tClass);
    }

    public static <T> List<T> listTo(List objs, Class<T> tClass) {
        List<T> list = Lists.newArrayList();
        for (Object obj : objs) {
            try {
                T t = tClass.newInstance();
                BeanCopyUtil.copyProperties(obj, t, false);
                list.add(t);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }
}
