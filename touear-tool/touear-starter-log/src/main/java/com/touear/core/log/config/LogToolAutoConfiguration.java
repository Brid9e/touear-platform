package com.touear.core.log.config;



import com.touear.core.log.aspect.LogTraceAspect;
import com.touear.core.log.filter.LogTraceFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.servlet.DispatcherType;

/**
 * 日志工具自动配置
 *
 * @author Chenl
 */
@Configuration
@ConditionalOnWebApplication
public class LogToolAutoConfiguration {


	@Bean
	public LogTraceAspect logTraceAspect() {
		return new LogTraceAspect();
	}


	@Bean
	public FilterRegistrationBean<LogTraceFilter> logTraceFilterRegistration() {
		FilterRegistrationBean<LogTraceFilter> registration = new FilterRegistrationBean<>();
		registration.setDispatcherTypes(DispatcherType.REQUEST);
		registration.setFilter(new LogTraceFilter());
		registration.addUrlPatterns("/*");
		registration.setName("LogTraceFilter");
		registration.setOrder(Ordered.LOWEST_PRECEDENCE);
		return registration;
	}



}
