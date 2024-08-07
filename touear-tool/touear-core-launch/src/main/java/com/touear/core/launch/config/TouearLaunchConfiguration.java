package com.touear.core.launch.config;

import lombok.AllArgsConstructor;
import com.touear.core.launch.props.TouearProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 配置类
 *
 */
@Configuration
@AllArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties({
	TouearProperties.class
})
public class TouearLaunchConfiguration {

}
