package com.touear.core.tool.exception;

import com.touear.core.tool.api.IResultCode;
import com.touear.core.tool.api.ResultCode;
import com.touear.core.tool.utils.PlaceholderUtil;
import com.touear.core.tool.utils.StringPool;

import lombok.Getter;


/**
 * 业务异常
 *
 * @author Synjones
 */
public class ServiceException extends RuntimeException {
	
	private static final long serialVersionUID = 2359767895161832954L;

	@Getter
	private final int code;
	
	@Getter
	private final String message;

	public ServiceException(IResultCode resultCode, Throwable cause) {
		super(cause);
		this.code = resultCode.getCode();
		this.message = resultCode.getMessage();
	}
	
	public ServiceException(IResultCode resultBean, String... params) {
		super(PlaceholderUtil.getResolver(StringPool.LEFT_BRACE, StringPool.RIGHT_BRACE)
				.resolverByIndex(resultBean.getMessage(), params));
		this.message = PlaceholderUtil.getResolver(StringPool.LEFT_BRACE, StringPool.RIGHT_BRACE)
				.resolverByIndex(resultBean.getMessage(), params);
		this.code = resultBean.getCode();
	}
	
	public ServiceException(String message) {
		super(message);
		this.code = ResultCode.FAILURE.getCode();
		this.message = message;
	}
	
	public ServiceException(int code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public ServiceException(int code, String message, String... params) {
		super(PlaceholderUtil.getResolver(StringPool.LEFT_BRACE, StringPool.RIGHT_BRACE).resolve(message, params));
		this.message = PlaceholderUtil.getResolver(StringPool.LEFT_BRACE, StringPool.RIGHT_BRACE).resolve(message, params);
		this.code = code;
	}

	/**
	 * 提高性能
	 *
	 * @return Throwable
	 */
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}

	public Throwable doFillInStackTrace() {
		return super.fillInStackTrace();
	}

}
