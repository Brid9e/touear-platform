package com.touear.common.constant;

/**
 * @author Chen
 */
public class SsoConstant {
	
	/**
     * 服务端登录地址
     */
	public static final String LOGIN_URL = "/login";

	/**
	 * 服务端登出地址
	 */
	public static final String LOGOUT_URL = "/logout";
	
	/**
	 * 服务端回调客户端地址参数名称
	 */
	public static final String REDIRECT_URI = "redirect_uri";
    
    /**
     * 服务端单点登出回调客户端登出参数名称
     */
    public static final String LOGOUT_PARAMETER_NAME = "logoutRequest";
    
    /**
     * 本地session中的AccessToken信息
     */
    public static final String SESSION_ACCESS_TOKEN = "_sessionAccessToken";
    
    /**
     * 模糊匹配后缀
     */
    public static final String URL_FUZZY_MATCH = "/*";
	
	/**
	 * 未登录或已过期
	 */
	public static final int NO_LOGIN = 2100;

	public static final String TGC = "TGC";
}
