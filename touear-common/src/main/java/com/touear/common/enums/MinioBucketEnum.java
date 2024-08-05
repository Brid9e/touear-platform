package com.touear.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StringUtils;

/**
 * @Title: minioBucketEnum.java
 * @Description: minio中桶名代码枚举类
 * 	注：上传文件时，需要验证桶名是否在此注册，若未注册，则不允许上传文件
 * @author Yang
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum MinioBucketEnum {
	//此处枚举桶名

    PHOTO("photo", "照片");


	;
	
	private String code;
	private String describe;

	/**
	 * 判断枚举中的code是否包含code
	 *
	 * @param code
	 * @return
	 */
	public static boolean contains(String code) {
		if (!StringUtils.isEmpty(code)) {
			MinioBucketEnum[] beanList = MinioBucketEnum.values();
			for (MinioBucketEnum item : beanList) {
		        if (code.equals(item.code)) {
		            return true;
		        }
		    }
		}
		return false;
	}

}
