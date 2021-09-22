package com.aaa.spock;

/**
 * @author liuzhen.tian
 * @version 1.0 Test.java  2021/8/27 20:18
 */
public class Test {
    public static void main(String[] args) {
        try {
            if(true){
                throw new Exception("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
