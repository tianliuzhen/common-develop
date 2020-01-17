package com.aaa.mybatisplus.entity;

import lombok.Data;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/1/14
 */
@Data
public class PageDto {
    private Integer current;
    private Integer size;

}
