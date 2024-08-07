package com.touear.core.launch.service;

import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * launcher 扩展 用于一些组件发现
 *
 * @author Chen
 */
public interface LauncherService {

	/**
	 * 启动时 处理 SpringApplicationBuilder
	 *
	 * @param builder SpringApplicationBuilder
	 * @param appName SpringApplicationAppName
	 * @param profile SpringApplicationProfile
	 * @param isLocalDev SpringApplicationIsLocalDev
	 */
	void launcher(SpringApplicationBuilder builder, String appName, String profile, boolean isLocalDev);

}
