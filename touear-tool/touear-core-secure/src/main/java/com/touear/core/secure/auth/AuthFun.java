package com.touear.core.secure.auth;

import com.touear.core.secure.User;
import com.touear.core.secure.constant.RoleConstant;
import com.touear.core.secure.utils.SecureUtil;
import com.touear.core.tool.utils.CollectionUtil;
import com.touear.core.tool.utils.Func;
import org.apache.commons.lang3.StringUtils;

/**
 * 权限判断
 *
 * @author Chen
 */
public class AuthFun {

	/**
	 * 放行所有请求
	 *
	 * @return {boolean}
	 */
	public boolean permitAll() {
		return true;
	}






}
