package com.aaa.commondevelop.config.global.exceptions;

/**
 * @author liuzhen.tian
 * @version 1.0 LimitException.java  2020/9/11 14:41
 */
public class LimitException extends RuntimeException {

    static final long serialVersionUID = 20190317;

    public LimitException () {
        super();
    }

    public LimitException (String s) {
        super (s);
    }

}
