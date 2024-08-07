package com.touear.core.oss.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.touear.core.tool.support.Kv;

import lombok.Data;

/**
 * Minio参数配置类
 *
 */
@Data
@ConfigurationProperties(prefix = "oss")
public class OssProperties {

	/**
	 * 是否启用
	 */
	private Boolean enable;

	/**
	 * 对象存储名称
	 */
	private String name;


	/**
	 * 对象存储服务的URL
	 */
	private String endpoint;

	/**
	 * Access key就像用户ID，可以唯一标识你的账户
	 */
	private String accessKey;

	/**
	 * Secret key是你账户的密码
	 */
	private String secretKey;

	/**
	 * 地址
	 */
	private String address;
	
	/**
	 * 域名
	 */
	private String domain;
	/**
	 * 获取文件对外访问地址
	 * @return
	 */
	public String getForeignAddress() {
		return address + "/minio";
	}

	/**
	 * 默认的存储桶名称
	 */
	private String bucketName = "touear";

	/**
	 * 自定义属性
	 */
	private Kv args;

}
