package com.touear.gateway.auth.config;

import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @Title: RibbonConfig.java
 * @Description: RibbonConfig
 * @author chenl
 * @date 2021-01-20 13:56:34
 * @version 1.0
 */
@Configuration
public class RibbonConfig {
 
	@Bean
	@Scope(value="prototype")
	public IRule ribbonRule() {
		return new WeightedResponseTimeRule();
	}
}