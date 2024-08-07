package com.touear.gateway.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限过滤
 *
 */
@Data
@RefreshScope
@ConfigurationProperties("touear.secure")
public class AuthProperties {
	/**
	 * 始终放行API集合
	 */
	private final List<String> skipUrl = new ArrayList<>();
    /**
     * 禁止访问API集合
     */
    private final List<String> forbidUrl = new ArrayList<>();
    /**
     * 是否开启签名验证
     */
    private Boolean checkSign = true;
    /**
     * 是否开启动态访问控制
     */
    private Boolean accessControl = true;

    /**
     * 是否开启接口调试
     */
    private Boolean apiDebug = false;

    /**
     * 无需鉴权的请求
     */
    private final List<String> authorityIgnores;

    /**
     * 签名忽略请求
     */
    private final List<String> signIgnores;

    /**
     * 允许跨域的地址
     */
    private String allowedOrigins = "*";
}
