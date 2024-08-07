package org.springframework.cloud.openfeign;

import feign.hystrix.HystrixFeign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * HystrixFeignTargeter 配置
 * @author L.cm
 */
@Configuration
@ConditionalOnClass(HystrixFeign.class)
@ConditionalOnProperty("feign.hystrix.enabled")
public class TouearHystrixFeignTargeterConfiguration {

	@Bean
	@Primary
	public Targeter bladeFeignTargeter() {
		return new TouearHystrixTargeter();
	}
}
