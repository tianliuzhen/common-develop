package com.aaa.mybatisplus.enums.common;

/**
 * @author soul
 */
public interface RestStatus {

    /**
     * the status codes of per restful request.
     *
     * @return 20xxx if succeed, 40xxx if client error, 50xxx if server side crash.
     */
    int code();

    /**
     * @return status enum name
     */
    String name();

    /**
     * @return message summary
     */
    String message();

}
