package com.touear.session;

import com.touear.common.AccessTokenContent;
import com.touear.common.AuthContent;
import com.touear.common.Expiration;
import com.touear.common.constant.SsoConstant;
import com.touear.core.tool.utils.HttpUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * 调用凭证AccessToken管理抽象
 * 
 * @author Chen
 */
public interface AccessTokenManager extends Expiration {

	/**
	 * 生成AccessToken
	 * 
	 * @param authContent
	 * @return
	 */
	default String generate(AuthContent authContent) {
		String accessToken = "AT-" + UUID.randomUUID().toString().replaceAll("-", "");
		create(accessToken, new AccessTokenContent(authContent.getTgt(), authContent.isSendLogoutRequest(),
				authContent.getRedirectUri()));
		return accessToken;
	}

	/**
	 * 生成AccessToken
	 * 
	 * @param accessToken
	 * @param accessTokenContent
	 */
	void create(String accessToken, AccessTokenContent accessTokenContent);
	
	/**
     * 延长AccessToken生命周期
     * 
	 * @param accessToken
	 * @return
	 */
	boolean refresh(String accessToken);
	
	/**
	 * 根据TGT删除AccessToken
	 * 
	 * @param tgt
	 */
	void remove(String tgt);
	
	/**
	 * 发起客户端登出请求
	 * 
	 * @param redirectUri
	 * @param accessToken
	 */
	default void sendLogoutRequest(String redirectUri, String accessToken) {
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put(SsoConstant.LOGOUT_PARAMETER_NAME, accessToken);
		HttpUtils.postHeader(redirectUri, headerMap);
	}
}
