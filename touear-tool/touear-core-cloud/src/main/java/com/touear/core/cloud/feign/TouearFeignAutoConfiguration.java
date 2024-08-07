package com.touear.core.cloud.feign;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.TouearFeignClientsRegistrar;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;

import com.netflix.hystrix.HystrixCommand;
import com.touear.core.tool.convert.EnumToStringConverter;
import com.touear.core.tool.convert.StringToEnumConverter;

import feign.Contract;
import feign.Feign;
import feign.RequestInterceptor;
import feign.hystrix.HystrixFeign;


/**
 * feign 增强配置
 *
 * @author L.cm
 */
@Configuration
@ConditionalOnClass(Feign.class)
@Import(TouearFeignClientsRegistrar.class)
@AutoConfigureAfter(EnableFeignClients.class)
public class TouearFeignAutoConfiguration {

	@Configuration("hystrixFeignConfiguration")
	@ConditionalOnClass({ HystrixCommand.class, HystrixFeign.class })
	protected static class HystrixFeignConfiguration {
		@Bean
		@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
		@ConditionalOnProperty("feign.hystrix.enabled")
		public Feign.Builder feignHystrixBuilder(
			RequestInterceptor requestInterceptor, Contract feignContract) {
			return HystrixFeign.builder()
				.contract(feignContract)
				.decode404()
				.requestInterceptor(requestInterceptor);
		}

		@Bean
		@ConditionalOnMissingBean
		public RequestInterceptor requestInterceptor() {
			return new TouearFeignRequestHeaderInterceptor();
		}
	}

	/**
	 *  enum 《-》 String 转换配置
	 * @param conversionService ConversionService
	 * @return SpringMvcContract
	 */
	@Bean
	public Contract feignContract(@Qualifier("mvcConversionService") ConversionService conversionService) {
		ConverterRegistry converterRegistry =  ((ConverterRegistry) conversionService);
		converterRegistry.addConverter(new EnumToStringConverter());
		converterRegistry.addConverter(new StringToEnumConverter());
		return new TouearSpringMvcContract(new ArrayList<>(), conversionService);
	}
}
