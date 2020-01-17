
package com.aaa.mybatisplus.config.configGlobalException.model;

import com.aaa.mybatisplus.config.configGlobalException.common.RestStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author soul
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" }, ignoreUnknown = true)
public class ErrorEntity implements Response {


	private static final long serialVersionUID = 3550224421750657701L;

	/**
	 * [M] 平台状态码
	 */
	@JsonProperty("code")
	private int code;

	/**
	 * [M] 错误信息
	 */
	@JsonProperty("msg")
	private String msg;

	private Object data;

	/**
	 * [C] 详细错误信息
	 */
	@JsonProperty("details")
	private Object details;

	public ErrorEntity(RestStatus statusCodes) {
		this(statusCodes, null);
	}

//	public ErrorEntity(RestStatus statusCodes, String msg) {
//		this.code = statusCodes.code();
//		this.msg = msg;
//	}

	public ErrorEntity(RestStatus statusCodes, Object details) {
		this.code = statusCodes.code();
		this.msg = statusCodes.message();
		if (details != null) {
			this.details = details;
		}
	}
}
