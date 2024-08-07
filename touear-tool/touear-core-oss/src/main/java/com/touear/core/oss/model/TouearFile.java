package com.touear.core.oss.model;

import lombok.Data;

/**
 * TouearFile
 *
 */
@Data
public class TouearFile {
	/**
	 * 文件地址
	 */
	private String link;
	/**
	 * 文件名
	 */
	private String name;
	/**
	 * 初始文件名
	 */
	private String originalName;
}
