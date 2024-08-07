package com.touear.core.secure.registry;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * secure api放行配置
 *
 * @author Chen
 */
@Data
@Component
public class SecureRegistry {

	private boolean enable = true;

	private final List<String> excludePatterns = new ArrayList<>();

	public SecureRegistry() {
		this.excludePatterns.add("/v2/api-docs/**");
		this.excludePatterns.add("/v2/api-docs-ext/**");
		this.excludePatterns.add("/auth/**");
		this.excludePatterns.add("/token/**");
		this.excludePatterns.add("/user/user-info");
		this.excludePatterns.add("/log/**");
		this.excludePatterns.add("/captcha.jpg");
		
	}

}
