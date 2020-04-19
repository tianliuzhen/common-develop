package com.aaa.mybatisplus.test.maps;

import java.util.Map;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/19
 */
class TestThread extends Thread{
    public Map map;
    public TestThread(Map map){
        this.map = map;
    }
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            map.put(this.getName() + i, i);
        }
        System.out.println(this.getName()+": "+map.size());
    }
}
