package com.touear.common;

/**
 * 有效期限管理器
 * 
 * @author Chen
 */
public interface ExpirationPolicy {
	
	/**
	 * 每5分钟执行一次
	 */
	public static final String SCHEDULED_CRON = "0 */5 * * * ?";
	
    /**
     * 定时清理
     */
    void verifyExpired();
}
