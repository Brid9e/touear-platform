package com.touear.core.oss.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.touear.core.oss.rule.TouearOssRule;
import com.touear.core.oss.rule.OssRule;

import lombok.AllArgsConstructor;

/**
 * Oss配置类
 *
 */
@Configuration
@AllArgsConstructor
@ConditionalOnProperty(value = "oss.enable", havingValue = "true")
public class OssConfiguration {


	@Bean
	@ConditionalOnMissingBean(OssRule.class)
	public OssRule ossRule() {
		return new TouearOssRule();
	}

}
