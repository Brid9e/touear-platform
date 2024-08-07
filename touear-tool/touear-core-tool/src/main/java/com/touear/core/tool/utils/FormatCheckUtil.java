package com.touear.core.tool.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Title: FormatCheckUtil.java
 * @Description: 正则验证
 * @author touear
 * @date 2018年10月29日 下午5:34:02
 * @version 1.0
 */
public class FormatCheckUtil {

	/**
	 * 邮箱格式校验
	 * 
	 * @param email
	 * @return boolean
	 */
	public static boolean emailFormatCheck(String email) {
		if (email == null || "".equals(email)) {
			return false;
		}
		String reg = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z\\.]{2,10})+$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	/**
	 * 手机号格式校验
	 * 
	 * @param mobile
	 * @return boolean
	 * @author hjin
	 * @cratedate 2013-9-10 上午10:37:33
	 */
	public static boolean mobileFormatCheck(String mobile) {
		if (mobile == null || "".equals(mobile)) {
			return false;
		}
		String reg = "^1[3|4|5|8][\\d]{9}$";
		return mobile.matches(reg);
	}
	
	/**
	 * 通配符匹配. eg: isWildMatch("abc*x*yz*", "abc123xxxyz") = true
	 * 
	 * @param wildcard
	 * @param str
	 * @return
	 */
	public static boolean isWildMatch(String wildcard, String str) {
		// 把通配符转成正则
		String regex = parseWildcard(wildcard);
		// 正则匹配str
		return str.matches(regex);
	}

	private static String parseWildcard(String wildcard) {
		// 特殊字符转译
		String result = "^";
		for (int i = 0; i < wildcard.length(); i++) {
			char ch = wildcard.charAt(i);
			if (ch == '*') {
				result += ".*";
			} else if (ch == '?') {
				result += ".{1}";
			} else if (isSpecial(ch)) {
				result += "\\" + ch;
			} else {
				result += ch;
			}
		}
		result += "$";
		return result;
	}

	private static boolean isSpecial(char ch) {
		char regexChar[] = { '$', '^', '[', ']', '(', ')', '{', '|', '+', '.',
				'\\' };
		for (int j = 0; j < regexChar.length; j++) {
			if (ch == regexChar[j]) {
				return true;
			}
		}
		return false;
	}
}
