package com.touear.core.secure.enums;

import com.touear.core.tool.utils.Func;

import com.touear.core.tool.utils.StringPool;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Title: LoginEnums.java
 * @Description: LoginEnums
 * @author chenl
 * @date 2020-12-17 11:11:26
 * @version 1.0
 */
public class LoginEnums {

	@Getter
	@AllArgsConstructor
	public enum LoginFromEnum {
		
		/**
		 * 从pc登录
		 */
		PC("pc", "pc端"),
		/**
		 * 从app登录
		 */
		APP("app", "App"),
		/**
		 * 从h5登录
		 */
		H5("h5", "App"),
		/**
		 * 从企业微信登录
		 */
		WECHAT_WORK("wechat-work", "企业微信"),

		/**
		 * 从微信公众号登录
		 */
		WECHAT_MP("wechat-mp", "公众号"),

		/**
		 * 从微信小程序登录
		 */
		WECHAT_MINA("wechat-mina", "小程序"),

		/**
		 * 从交行小程序登录
		 */
		BCMMiniPro("BCMMiniPro", "交行小程序"),
		/**
		 * 设备登录
		 */
		EQ("eq", "设备"),

		/**
		 * 管理员
		 */
		MANAGEMENT("management", "管理员"),

		/**
		 * 从支付宝生活号登录
		 */
		ALIPAY_LIFE("alipay-life", "生活号"),

		/**
		 * 从支付宝小程序登录
		 */
		ALIPAY_MINA("alipay-mina", "小程序"),

		/**
		 * 自助终端登录
		 */
		SELF_SERVICE("selfService","自助终端"),

		/**
		 * 蓝信APP
		 */
		LANXIN("lanxin","蓝信APP"),


		;

		/**
		 * 类型
		 */
		private String type;
		/**
		 * 描述
		 */
		private String name;

		/**
		 * 根据type获取对应的枚举对象
		 * @param matchingRule		匹配规则【0：完全相等；1：以loginfrom.type开始】,默认0
		 * @return {@link LoginFromEnum}
		 * @author wangqq
		 * @date 2021-09-27 11:54:25
		 */
		public static LoginFromEnum of(String type, String ... matchingRule) {
			if (Func.isBlank(type)) {
				return null;
			}
			String rule = null;
			if (matchingRule != null && matchingRule.length == 1) {
				if (StringPool.ZERO.equals(matchingRule[0]) || StringPool.ONE.equals(matchingRule[0])) {
					rule = matchingRule[0];
				}
			}
			if (rule == null) {
				rule = StringPool.ZERO;
			}
			LoginFromEnum[] values = LoginFromEnum.values();
			for (LoginFromEnum fromEnum : values) {
				if (StringPool.ZERO.equals(rule) && Func.equals(fromEnum.type, type)) {
					return fromEnum;
				} else if (StringPool.ONE.equals(rule) && type.startsWith(fromEnum.type)) {
					return fromEnum;
				}
			}
			return null;
		}
	}

	/**
	 * 登录类型
	 */

	@Getter
	@AllArgsConstructor
	public enum LoginTypeEnum {

		/**
		 * 第三方登录
		 */
		CAS("cas", "第三方登录"),

		CAS_ACCOUNT("casAccount", "一卡通账号"),

		CAS_IDNUMBER("casIdNumber", "身份证号(刷身份证)"),

		CAS_IDNUMBER_SNO("casIdNumberSno", "刷身份证+学号"),

		CAS_ID_NUMBER("casId", "身份证号"),
		/**
		 * 账号
		 */
		ACCOUNT_ID("accountId", "账号登录"),
		/**
		 * 手机
		 */
		MOBILE("mobile", "手机微信端"),
		/**
		 * k12管理端
		 */
		K_TWELVE_MANAGE("kTwelveManage","k12管理端"),
		/**
		 * k12app端
		 */

		K_TWELVE("kTwelve","k12app端");

		/**
		 * 类型
		 */
		private String type;
		/**
		 * 描述
		 */
		private String name;

		/**
		 * 根据type获取对应的枚举对象
		 * @param matchingRule		匹配规则【0：完全相等；1：以loginType.type开始】,默认0
		 * @return {@link LoginTypeEnum}
		 */
		public static LoginTypeEnum of(String type, String ... matchingRule) {
			if (Func.isBlank(type)) {
				return null;
			}
			String rule = null;
			if (matchingRule != null && matchingRule.length == 1) {
				if (StringPool.ZERO.equals(matchingRule[0]) || StringPool.ONE.equals(matchingRule[0])) {
					rule = matchingRule[0];
				}
			}
			if (rule == null) {
				rule = StringPool.ZERO;
			}
			LoginTypeEnum[] values = LoginTypeEnum.values();
			for (LoginTypeEnum typeEnum : values) {
				if (StringPool.ZERO.equals(rule) && Func.equals(typeEnum.type, type)) {
					return typeEnum;
				} else if (StringPool.ONE.equals(rule) && type.startsWith(typeEnum.type)) {
					return typeEnum;
				}
			}
			return null;
		}
	}

}
