package com.touear.gateway.config;

import com.touear.core.jwt.JwtUtil;
import com.touear.core.jwt.props.JwtProperties;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Jwt配置类
 *
 */
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties({JwtProperties.class})
public class JwtConfiguration implements SmartInitializingSingleton {

	private final JwtProperties jwtProperties;

	@Override
	public void afterSingletonsInstantiated() {
		JwtUtil.setJwtProperties(jwtProperties);
	}
}
