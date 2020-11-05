package com.aaa.mybatisplus.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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

    @ApiModelProperty("排序字段")
    private String orderBy;

    @ApiModelProperty("排序方式 asc/desc")
    private String direction = "desc";
}
