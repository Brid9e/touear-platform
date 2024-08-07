package com.touear.core.secure.props;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * secure放行额外配置
 *
 * @author Chen
 */
@Data
@ConfigurationProperties("touear.secure.url")
public class SecureProperties {

	private final List<String> excludePatterns = new ArrayList<>();

}
