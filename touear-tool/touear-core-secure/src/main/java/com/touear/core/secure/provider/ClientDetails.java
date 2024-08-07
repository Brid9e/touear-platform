package com.touear.core.secure.provider;


import lombok.Data;

/**
 * 客户端详情
 *
 * @author Chen
 */
@Data
public class ClientDetails implements IClientDetails {

	/** TODO */
	private static final long serialVersionUID = -5487466866280705214L;
	/**
	 * 客户端id
	 */

	private String clientId;
	/**
	 * 客户端密钥
	 */

	private String clientSecret;

	/**
	 * 令牌过期秒数
	 */

	private Integer accessTokenValidity;
	/**
	 * 刷新令牌过期秒数
	 */

	private Integer refreshTokenValidity;

}
