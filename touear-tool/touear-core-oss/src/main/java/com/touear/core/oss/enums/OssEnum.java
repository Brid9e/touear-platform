package com.touear.core.oss.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Oss枚举类
 *
 */
@Getter
@AllArgsConstructor
public enum OssEnum {

	/**
	 * minio
	 */
	MINIO("minio", 1),

	;

	final String name;
	final int category;

}
