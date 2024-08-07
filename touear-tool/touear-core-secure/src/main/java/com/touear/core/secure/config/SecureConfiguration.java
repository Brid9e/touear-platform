package com.touear.core.secure.config;


import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.touear.core.secure.aspect.AuthAspect;
//import com.touear.core.secure.interceptor.SecureInterceptor;
import com.touear.core.secure.props.SecureProperties;
import com.touear.core.secure.provider.ClientDetailsServiceImpl;
import com.touear.core.secure.provider.IClientDetailsService;
import com.touear.core.secure.registry.SecureRegistry;

import lombok.AllArgsConstructor;

/**
 * 安全配置类
 *
 * @author Chen
 */
@Order
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(SecureProperties.class)
public class SecureConfiguration implements WebMvcConfigurer {

	private final SecureRegistry secureRegistry;

	private final SecureProperties secureProperties;


	private final JdbcTemplate jdbcTemplate;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

//		if (secureRegistry.isEnable()) {
//			registry.addInterceptor(new SecureInterceptor())
//				.excludePathPatterns(secureRegistry.getExcludePatterns())
//				.excludePathPatterns(secureProperties.getExcludePatterns());
//		}
	}

	@Bean
	public AuthAspect authAspect() {
		return new AuthAspect();
	}

	@Bean
	@ConditionalOnMissingBean(IClientDetailsService.class)
	public IClientDetailsService clientDetailsService() {
		return new ClientDetailsServiceImpl(jdbcTemplate);
	}

}
