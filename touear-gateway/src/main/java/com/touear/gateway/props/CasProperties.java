package com.touear.gateway.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 客户端校验配置
 *
 * @author Chen
 */
@Data
@ConfigurationProperties("berserker.cas")
public class CasProperties {

	private String casLoginUrl;

	private String casLogoutUrl;

}
