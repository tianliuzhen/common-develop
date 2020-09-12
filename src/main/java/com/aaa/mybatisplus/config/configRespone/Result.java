package com.aaa.mybatisplus.config.configRespone;

import com.aaa.mybatisplus.enums.common.ResultStatus;
import lombok.Getter;
import lombok.ToString;

/**
 * 自定义 状态码
 * 因为有些场景会遇到同一个项目里面想要不同的返回值
 * {"code":200,"message":"OK","content":1}
 * {"code":200,"desc":"OK","result":1}
 * ...
 * @author liuzhen.tian
 * @version 1.0 Result.java  2020/9/12 23:10
 */
@Getter
@ToString
public class Result<T>  implements Response{
    /** 业务错误码 */
    private Integer code;
    /** 信息描述 */
    private String desc;
    /** 返回参数 */
    private T content;

    private Result(ResultStatus resultStatus, T data) {
        this.code = resultStatus.getCode();
        this.desc = resultStatus.getMessage();
        this.content = data;
    }

    /** 业务成功返回业务代码和描述信息 */
    public static Result<Void> success() {
        return new Result<Void>(ResultStatus.SUCCESS, null);
    }

    /** 业务成功返回业务代码,描述和返回的参数 */
    public static <T> Result<T> success(T data) {
        return new Result<T>(ResultStatus.SUCCESS, data);
    }

    /** 业务成功返回业务代码,描述和返回的参数 */
    public static <T> Result<T> success(ResultStatus resultStatus, T data) {
        if (resultStatus == null) {
            return success(data);
        }
        return new Result<T>(resultStatus, data);
    }

    /** 业务异常返回业务代码和描述信息 */
    public static <T> Result<T> failure() {
        return new Result<T>(ResultStatus.INTERNAL_SERVER_ERROR, null);
    }

    /** 业务异常返回业务代码,描述和返回的参数 */
    public static <T> Result<T> failure(ResultStatus resultStatus) {
        return failure(resultStatus, null);
    }

    /** 业务异常返回业务代码,描述和返回的参数 */
    public static <T> Result<T> failure(ResultStatus resultStatus, T data) {
        if (resultStatus == null) {
            return new Result<T>(ResultStatus.INTERNAL_SERVER_ERROR, null);
        }
        return new Result<T>(resultStatus, data);
    }
}
