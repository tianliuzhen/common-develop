package com.aaa.mybatisplus.test.extendsAndSuper;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/1/14
 */
public   class B<T> extends A{
    private T result;
    public int num=0;
    public B() {
        super(123);
        System.out.println("B的无参构造器");
    }
    public B(T result) {
        this.result=result;
        System.out.println("B的有参构造器");
    }
}
