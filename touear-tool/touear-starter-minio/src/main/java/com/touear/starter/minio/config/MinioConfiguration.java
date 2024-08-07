package com.touear.starter.minio.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.touear.core.oss.config.OssConfiguration;
import com.touear.core.oss.props.OssProperties;
import com.touear.core.oss.rule.TouearOssRule;
import com.touear.core.oss.rule.OssRule;
import com.touear.starter.minio.MinioTemplate;

import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

/**
 * Minio配置类
 *
 */
@Configuration
@AllArgsConstructor
@AutoConfigureAfter(OssConfiguration.class)
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(value = "oss.name", havingValue = "minio")
public class MinioConfiguration {

	private OssProperties ossProperties;

	@Bean
	@ConditionalOnMissingBean(OssRule.class)
	public OssRule ossRule() {
		return new TouearOssRule();
	}

	@Bean
	@SneakyThrows
	@ConditionalOnMissingBean(MinioClient.class)
	public MinioClient minioClient() {
		return new MinioClient(
			ossProperties.getEndpoint(),
			ossProperties.getAccessKey(),
			ossProperties.getSecretKey()
		);
	}

	@Bean
	@ConditionalOnBean({MinioClient.class, OssRule.class})
	@ConditionalOnMissingBean(MinioTemplate.class)
	public MinioTemplate minioTemplate(MinioClient minioClient, OssRule ossRule) {
		return new MinioTemplate(minioClient, ossRule, ossProperties);
	}

}
