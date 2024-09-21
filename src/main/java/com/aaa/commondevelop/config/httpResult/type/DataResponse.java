package com.aaa.commondevelop.config.httpResult.type;

import com.aaa.commondevelop.config.httpResult.RestfulResponse;
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
public class DataResponse<T> extends RestfulResponse {

    private static final long serialVersionUID = 3866194250704048829L;

    @ApiModelProperty(value = "接口响应数据")
    private T data;

    /**
     * 业务成功返回业务代码,描述和返回的参数
     */
    public static <T> DataResponse<T> success(T data) {
        return new DataResponse<T>(data);
    }

    public DataResponse(T data) {
        super();
        this.data = data;

    }
}
