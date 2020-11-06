package com.aaa.mybatisplus.domain.ao;

import com.aaa.mybatisplus.domain.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author liuzhen.tian
 * @version 1.0 PageAo.java  2020/11/5 15:13
 */
@Data
public class PageAo {
    @ApiModelProperty("页码")
    private int pageIndex = 1;

    @ApiModelProperty("页面行数")
    private int pageSize = 10;

    @ApiModelProperty("排序字段,如果多个字段可改为数组")
    private String orderBy[];

    @ApiModelProperty("排序方式 asc/desc")
    private String direction;

    @ApiModelProperty("查询条件")
    private  Map<String, String> condition;

    public QueryWrapper<User> getQueryWrapper(){
        QueryWrapper<User> queryWrapper=new QueryWrapper<User>();

        // eq 匹配字段 ,默认不忽略是空，如果忽略空加 false 即可
        queryWrapper.allEq(this.condition, false);

        // 排序设置
        if (orderBy.length > 0) {
            if (StringUtils.isNotBlank(direction) && StringUtils.isNotBlank(direction)) {
                if (direction.equals("asc")) {
                    queryWrapper.orderByAsc(orderBy);
                }
                if (direction.equals("desc")) {
                    queryWrapper.orderByDesc( orderBy);
                }
            }

        }


        return queryWrapper;
    }

    public Page<User> getPage(){

        Page<User> page = new Page<User>();
        if (this.pageIndex < 1) {
            this.pageIndex = 1;
        }
        if (this.pageSize < 1) {
            this.pageSize = 10;
        }
        page.setSize(this.pageSize);
        page.setCurrent(this.pageIndex);
        //查询条件
        if (condition!=null) {
        }

        // 排序设置 => 此种排序官方已经废弃，所以用其他方式（queryWrapper）
        // if (StringUtils.isNotBlank(orderBy)) {
        //     if (StringUtils.isNotBlank(direction) && StringUtils.isNotBlank(direction)) {
        //             if (direction.equals("desc")) {
        //                 page.setDesc(orderBy);
        //             }else {
        //                 page.setAsc(orderBy);
        //             }
        //     }
        //
        // }
        return page;
    }
}
