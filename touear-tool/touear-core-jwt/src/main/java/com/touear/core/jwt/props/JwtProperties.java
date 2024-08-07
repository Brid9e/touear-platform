package com.touear.core.jwt.props;

import com.touear.core.jwt.constant.JwtConstant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWT配置
 *
 */
@Slf4j
@Data
@ConfigurationProperties("touear.token")
public class JwtProperties {

	/**
	 * token签名
	 */
	private String signKey = JwtConstant.DEFAULT_SECRET_KEY;

	/**
	 * 获取签名规则
	 */
	public String getSignKey() {
		/*if (this.signKey.length() < JwtConstant.SECRET_KEY_LENGTH) {
			log.warn("Token已启用默认签名,请前往blade.token.sign-key设置32位的key");
			return JwtConstant.DEFAULT_SECRET_KEY;
		}*/
		return this.signKey;
	}

}
