package com.touear.core.jwt.constant;

/**
 * Jwt常量
 *
 * @author Synjones
 */
public interface JwtConstant {

	/**
	 * 默认key
	 */
	String DEFAULT_SECRET_KEY = "Touear#123@502";

	/**
	 * key安全长度，具体见：https://tools.ietf.org/html/rfc7518#section-3.2
	 */
	int SECRET_KEY_LENGTH = 32;

}
