package com.aaa.spock

import com.aaa.spock.data.DataUtils
import spock.lang.Specification

/**
 * 参考： https://zhuanlan.zhihu.com/p/148371539
 */
class DataTestSpec extends Specification {


    def "test -- 两个小数加法计算"() {
        //期望结果
        expect:
        //可以看做精简版的when+then
        DataUtils.add(a, b) == result

        //计算条件
        where:
        a       | b   | result
        1       | 2   | 3
        2       | 2   | 4
    }

}
