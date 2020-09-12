package com.aaa.mybatisplus.config.configRespone.type;

import com.aaa.mybatisplus.config.configRespone.RestfulResponse;
import io.swagger.annotations.ApiModel;
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
@ApiModel("相应数据")
public class ResultResponse<T> extends RestfulResponse {

    private static final long serialVersionUID = 3866194250704048829L;

    @ApiModelProperty(value = "接口响应数据")
    private T result;

    /** 业务成功返回业务代码,描述和返回的参数 */
    public static <T> ResultResponse<T> success(T data) {
        return new ResultResponse<T>(data);
    }

    public ResultResponse(T result) {
        super();
        this.result = result;

    }
}
