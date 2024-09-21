package com.aaa.commondevelop.service.common;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author liuzhen.tian
 * @version 1.0 MyFunction.java  2023/7/30 18:12
 */
public interface MyFun<T, R> extends Function<T, R>, Serializable {
}
