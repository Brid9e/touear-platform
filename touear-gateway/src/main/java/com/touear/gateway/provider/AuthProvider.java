package com.touear.gateway.provider;

import java.util.ArrayList;
import java.util.List;

public class AuthProvider {

	public static String TARGET = "/**";
	public static String REPLACEMENT = "";
	public static String AUTH_KEY = "touear-auth";
	/** 不用token拦截的地址 */
	private static final List<String> defaultSkipUrl = new ArrayList<>();

	/** 不用授权的地址 */
	private static final List<String> defaultAuthUrl = new ArrayList<>();
	private static final List<String> appAuthUrl = new ArrayList<>();
	private static final List<String> h5AuthUrl = new ArrayList<>();



	static {
		//不拦截
		defaultSkipUrl.add("/touear-auth/oauth/token/**");
		defaultSkipUrl.add("/oauth/token/**");
		defaultSkipUrl.add("/token/**");
		defaultSkipUrl.add("/actuator/health/**");
		defaultSkipUrl.add("/v2/api-docs/**");
		defaultSkipUrl.add("/v2/api-docs-ext/**");
		defaultSkipUrl.add("/auth/**");
		defaultSkipUrl.add("/log/**");
		defaultSkipUrl.add("/menu/routes");
		defaultSkipUrl.add("/menu/auth-routes");
		defaultSkipUrl.add("/menu/top-menu");

		//不判断授权
		//wechat/token/mp
		defaultAuthUrl.add("/wechat/token/**");
		defaultAuthUrl.add("/cas/**");
		defaultAuthUrl.add("/charge/**");
		defaultAuthUrl.add("/login/**");
	}

	/**
	 * 默认无需鉴权的API
	 */
	public static List<String> getDefaultSkipUrl() {
		return defaultSkipUrl;
	}

	public static List<String> getDefaultAuthUrl() {
		return defaultAuthUrl;
	}
	public static List<String> getAppAuthUrl() {
		return appAuthUrl;
	}
	public static List<String> getH5AuthUrl() {
		return h5AuthUrl;
	}

}
