package com.touear.starter.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @title: RedisCacheProperties
 * @description: Redis缓存有效期配置属性
 * @version: 1.0
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "touear.cache")
public class RedisCacheProperties {

    private Duration defaultValidity = Duration.ofMinutes(10L);

    private Map<String, Duration> validity = new HashMap<>();
}
