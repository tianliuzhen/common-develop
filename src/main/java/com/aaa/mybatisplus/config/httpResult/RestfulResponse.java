package com.aaa.mybatisplus.config.httpResult;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public abstract class RestfulResponse implements Response {

    private static final long serialVersionUID = -7443304902819898146L;

    @ApiModelProperty(value = "响应状态码",example = "200")
    @JsonProperty("code")
    private int code = 200;

    @ApiModelProperty(value = "响应消息",example = "success")
    @JsonProperty("message")
    private String message = "success";

}
