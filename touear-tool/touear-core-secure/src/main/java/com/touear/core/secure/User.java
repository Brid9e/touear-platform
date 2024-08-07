package com.touear.core.secure;

import java.io.Serializable;
import java.util.List;


import lombok.Data;

/**
 * 用户实体
 *

 */
@Data
public class User implements Serializable {

	private static final long serialVersionUID = 1L;


	/** 用户id */
	private Long id;

	/** 姓名 */
	private String name;

	/** 账号 */
	private String account;

	/** 登录来源 */
	private String loginFrom;

}
