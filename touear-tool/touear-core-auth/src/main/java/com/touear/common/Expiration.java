package com.touear.common;

/**
 * 含时效
 * 
 * @author Chen
 */
public interface Expiration {
	
	/**
	 * 时效（秒）
	 * @return
	 */
	int getExpiresIn();
}
