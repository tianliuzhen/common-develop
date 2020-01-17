package com.aaa.mybatisplus.config.configRespone;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 自定义返回值
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/1/14
 */
@Data
@ApiModel("相应数据")
public class ObjectDataResponse<T> extends RestfulResponse {

    private static final long serialVersionUID = 3866194250704048829L;

    @ApiModelProperty(value = "接口响应数据")
    private T data;

    public ObjectDataResponse(T data) {
        super();
        this.data = data;

    }
}
