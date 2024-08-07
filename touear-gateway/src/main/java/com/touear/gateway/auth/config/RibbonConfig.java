package com.touear.gateway.auth.config;

import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @Title: RibbonConfig.java
 * @Description: RibbonConfig
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