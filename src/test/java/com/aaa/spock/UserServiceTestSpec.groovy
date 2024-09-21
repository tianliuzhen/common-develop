package com.aaa.spock

import com.aaa.commondevelop.domain.entity.User
import com.aaa.commondevelop.mapper.UserMapper
import com.aaa.commondevelop.service.impl.User2ServiceImpl
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.google.common.collect.Lists
import spock.lang.Specification


class UserServiceTestSpec extends Specification {
    def userService = new User2ServiceImpl()


    //注入mock数据
    def setup() {
        userService.userMapper = Mock(UserMapper)
    }

    def "测试 user"() {
        given:
        //准备数据
        userService.userMapper.selectPageVo(_, _) >>
                new Page<User>(records: Lists.asList(
                        new User("id": 1, "name": "小米"),
                        new User("id": 2, "name": "小三")
                ))

        expect:
        def result = userService.selectUserPage(input1, input2)
        result.getSize() == 10
        result.records.size() == 2

        where:
        input1 = new Page<User>(size: 10, current: 1)
        input2 = "小三"

    }

    def "测试 异常"() {
        given:
        //准备数据
        userService.userMapper.selectPageVo(_, _) >> {
            throw new RuntimeException("spock 异常")
        }

        when:
        userService.selectUserPage(null, null)

        then:
        def exception = thrown(RuntimeException.class)
        exception.message == "spock 异常"
    }

}

