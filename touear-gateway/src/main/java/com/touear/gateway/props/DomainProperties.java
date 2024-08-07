package com.touear.gateway.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 域名配置
 *
 */
@Data
@Component
@ConfigurationProperties("oss")
public class DomainProperties {

	/**
	 * 域名
	 */
	private String address;

}
