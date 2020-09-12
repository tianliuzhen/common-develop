package com.aaa.mybatisplus.config.httpResult.type;

import com.aaa.mybatisplus.config.httpResult.RestfulResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/1/14
 */
@Data
public class PageResponse<T> extends RestfulResponse {

    private static final long serialVersionUID = -3941953974584339445L;

    @ApiModelProperty(value = "接口响应数据（分页）")
    private Page<T> data;

    public PageResponse(Page<T> data) {

        super();

        this.data = data;

    }
}
