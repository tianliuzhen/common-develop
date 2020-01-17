package com.aaa.mybatisplus.test;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/1/14
 */
 class test extends B {
    int n=super.num;
    public test() {
    }

    public test(Object result) {

        super(result);
    }

    public static void main(String[] args) {

        String s="";
        B bb = new B(s);
        System.out.println(bb.toString());
    }

}
