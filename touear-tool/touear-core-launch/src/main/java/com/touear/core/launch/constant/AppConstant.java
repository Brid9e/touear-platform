package com.touear.core.launch.constant;

/**
 * 系统常量
 *
 * @author Yang
 */
public interface AppConstant {

	/**
	 * 应用版本
	 */
	String APPLICATION_VERSION = "1.0.0";

	/**
	 * 基础包
	 */
	String BASE_PACKAGES = "com.touear";

	/**
	 * 应用名前缀
	 */
	String APPLICATION_NAME_PREFIX = "touear-";
	/**
	 * 网关模块名称
	 */
	String APPLICATION_GATEWAY_NAME = APPLICATION_NAME_PREFIX + "gateway";
	/**
	 * 授权模块名称
	 */
	String APPLICATION_AUTH_NAME = APPLICATION_NAME_PREFIX + "auth";
	/**
	 * 管理端模块名称
	 */
	String APPLICATION_MANAGE_NAME = APPLICATION_NAME_PREFIX + "manage";

	
	/**
	 * 客户端模块名称
	 */
	String APPLICATION_USER_NAME = APPLICATION_NAME_PREFIX + "client";

	/**
	 * 开发环境
	 */
	String DEV_CDOE = "dev";
	/**
	 * 生产环境
	 */
	String PROD_CODE = "prod";
	/**
	 * 测试环境
	 */
	String TEST_CODE = "test";

	/**
	 * 代码部署于 linux 上，工作默认为 mac 和 Windows
	 */
	String OS_NAME_LINUX = "LINUX";

}
