package com.touear.common.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: RedisConstants.java
 * @Description: redis key常数类

 * @version 1.0
 */
public class RedisConstants {

	/**
	 * @Title: RedisConstant.java
	 * @Description: 系统参数
	 * @author chenl
	 * @date 2020-08-20 09:44:58
	 * @version 1.0
	 */
	public static class SysConfigConstant {
		/** 系统参数 */
		public static final String SYSTEM_CONFIG = "sysconfig";
		/** 登录页面 */
		public static final String APP_LOGIN_URL = "app_login_url";
		/** app登出页面 */
		public static final String APP_LOGOUT_URL = "app_logout_url";
		/** pc登出页面 */
		public static final String PC_LOGOUT_URL = "pc_logout_url";
		/** pc登录页面 */
		public static final String PC_LOGIN_URL = "pc_login_url";
	}

	/**
	 * @Title: RedisConstant.java
	 * @Description: 用户
	 * @author chenl
	 * @date 2020-08-20 09:59:22
	 * @version 1.0
	 */
	public static class UserConstant {
		/** 用户 */
		public final static String USER = "user:";

		public final static String TOKEN = USER+"token:";
		public final static String LOGIN = USER+"jwt:";

		/** 用户个人缓存 */
		public final static String USER_CACHE = USER + "cache:";

		/** 用户个人绑定账号 */
		public final static String USER_BAND_ACCOUNT = USER + "bandAccount:";
		/**
		 * 前端的用户个人可清理的缓存数据
		 */
		public final static String WEB_CACHE = USER + "web_cache:";
		public final static String DELETABLE = "deletable";
		public final static String DELETABLE_KEY = WEB_CACHE + DELETABLE;
		/**
		 * 前端的用户个人不可清理的缓存数据
		 */
		public final static String UNDELETABLE = "undeletable";
		/**
		 * 前端缓存集合
		 * 分为两类，一类为可清理的，一类为不可清理的
		 */
		public final static List<String> webCacheKeyList = new ArrayList<String>() {
			{
				add(DELETABLE);
				add(UNDELETABLE);
			}
		};
		/** 用户信息 */
		public final static String USER_INFO = USER + "info:";

		/** 用户登录openid编号 */
		public final static String USER_OPENID= USER_INFO + "wechatopenid:";
		/** 用户信息 、权限 */
		public final static String USER_ACCOUNT = USER + "account:";

	}

}
