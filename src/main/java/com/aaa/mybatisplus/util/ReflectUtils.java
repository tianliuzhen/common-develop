package com.aaa.mybatisplus.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * description: 反射赋值工具类
 *
 * @author 田留振(tianliuzhen @ haoxiaec.com)
 * @version 1.0
 * @date 2019/7/10
 */
public class ReflectUtils {

    public  static void setValue(Object model,String setName,String setValue){
       // 获取实体类的所有属性，返回Field数组
        Field[] field = model.getClass().getDeclaredFields();
        try {
            // 遍历所有属性
            for (int j = 0; j < field.length; j++) {
                // 获取属性的名字
                String name = field[j].getName();
                if (name.equals(setName)){
                    // 将属性的首字符大写，方便构造get，set方法
                    name = name.substring(0, 1).toUpperCase() + name.substring(1);
                    // 获取属性的类型
                    String type = field[j].getGenericType().toString();
                    if (type.equals("class java.lang.String")) {
                        // 如果type是类类型，则前面包含"class "，后面跟类名
                        Method m = model.getClass().getMethod("get" + name);
                        String value = (String) m.invoke(model);
                        // 调用getter方法获取属性值
                        if (value == null) {
                            m = model.getClass().getMethod("set"+name,String.class);
                            m.invoke(model, setValue.toString());
                        }
                    }
                    if (type.equals("class java.lang.Integer")) {
                        Method m = model.getClass().getMethod("get" + name);
                        Integer value = (Integer) m.invoke(model);
                        if (value == null) {
                            m = model.getClass().getMethod("set"+name,Integer.class);
                            m.invoke(model, Integer.parseInt(setValue));
                        }
                    }
                    if (type.equals("class java.lang.Long")) {
                        Method m = model.getClass().getMethod("get" + name);
                        Long value = (Long) m.invoke(model);
                        if (value == null) {
                            m = model.getClass().getMethod("set"+name,Integer.class);
                            m.invoke(model, Long.parseLong(setValue));
                        }
                    }
                    if (type.equals("class java.lang.Boolean")) {
                        Method m = model.getClass().getMethod("get" + name);
                        Boolean value = (Boolean) m.invoke(model);
                        if (value == null) {
                            m = model.getClass().getMethod("set"+name,Boolean.class);
                            m.invoke(model, Boolean.parseBoolean(setValue));
                        }
                    }
                    if (type.equals("class java.util.Date")) {
                        Method m     = model.getClass().getMethod("get" + name);
                        Date value = (Date) m.invoke(model);
                        if (value == null) {
                            m = model.getClass().getMethod("set"+name,Date.class);
                            SimpleDateFormat sdf    = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            m.invoke(model, sdf.parse(setValue));
                        }
                    }// 如果有需要,可以仿照上面继续进行扩充,再增加对其它类型的判断
                    break;//跳出较少循环
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
