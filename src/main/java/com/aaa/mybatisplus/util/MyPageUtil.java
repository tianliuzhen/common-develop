package com.aaa.mybatisplus.util;

import com.aaa.mybatisplus.domain.entity.User;
import lombok.Data;
import org.assertj.core.util.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 分页工具类
 *
 * @author liuzhen.tian
 * @version 1.0 MyPageUtil.java  2022/5/3 18:49
 */

@Data
public class MyPageUtil<T> {

    /**
     * 泛型数据
     */
    private List<T> list;

    /**
     * 每页数量
     */
    private Integer pageSize;

    /**
     * 当前页
     */
    private Integer pageNo;

    /**
     * 总页数
     */
    private Integer totalPages;

    /**
     * 总条数
     */
    private int totalCount;


    /**
     * 总页数
     *
     * @param totalCount 总条数
     * @param pageSize   每页数量
     * @return int
     */
    public static int totalPage(int totalCount, int pageSize) {
        if (pageSize == 0) {
            return 0;
        } else {
            return totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        }

    }

    /**
     * 【内存分页】 初始化构造函数
     *
     * @param pageNo   当前页
     * @param pageSize 每页数量
     * @param list     初始化数据
     */
    public MyPageUtil(Integer pageNo, Integer pageSize, List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        // 每页数量
        this.pageSize = pageSize;
        // 当前页
        this.pageNo = pageNo;
        // 总条数
        this.totalCount = list.size();
        // 总页数
        this.totalPages = totalPage(list.size(), pageSize);

        // 第几页处理
        if (pageNo > this.totalPages) {
            this.pageNo = this.totalPages;
        }

        // 起始索引
        int fromIndex = (this.pageNo - 1) * pageSize;

        // 结束索引
        int toIndex = this.pageNo * this.pageSize;
        toIndex = toIndex > this.totalCount ? this.totalCount : toIndex;

        this.list = list.subList(fromIndex, toIndex);
    }

    public static void main(String[] args) {
        List<User> list = Lists.newArrayList();
        for (int i = 0; i < 19; i++) {
            list.add(new User(i + "", "name"));
        }

        MyPageUtil myPageUtil = new MyPageUtil(1,10,list);
        System.out.println(myPageUtil.getList());
    }
}
