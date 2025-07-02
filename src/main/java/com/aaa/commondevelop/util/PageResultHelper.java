package com.aaa.commondevelop.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author liuzhen.tian
 * @version 1.0 PageResultHelper.java  2025/7/2 20:43
 */
public class PageResultHelper {

    /**
     * page分页
     *
     * @param pageNum  当前页
     * @param pageSize 页数
     * @param queryDb  查询返回结果
     * @param clazz    映射类
     * @param <T>      泛型
     * @return PageInfo<T>
     */
    public static <T> PageInfo<T> toPageInfo(int pageNum,
                                             int pageSize,
                                             Supplier<List> queryDb,
                                             Class<T> clazz) {
        Page<T> page = PageHelper.startPage(pageNum, pageSize);
        List list = queryDb.get();
        // 此时list =  page
        PageInfo<T> result = new PageInfo<>(list);
        result.setList(BeanConvertUtil.listTo(list, clazz));
        return result;
    }
}
