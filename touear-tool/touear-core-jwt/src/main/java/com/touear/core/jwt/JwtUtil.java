package com.touear.core.jwt;

import com.touear.core.jwt.props.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Jwt工具类
 *
 * @author Synjones
 */
public class JwtUtil {

	/**
	 * token基础配置
	 */
	public static String BEARER = "bearer";
	public static Integer AUTH_LENGTH = 7;


	/**
	 * jwt配置
	 */
	private static JwtProperties jwtProperties;


	public static JwtProperties getJwtProperties() {
		return jwtProperties;
	}

	public static void setJwtProperties(JwtProperties properties) {
		if (JwtUtil.jwtProperties == null) {
			JwtUtil.jwtProperties = properties;
		}
	}


	/**
	 * 签名加密
	 */
	public static String getBase64Security() {
		return Base64.getEncoder().encodeToString(getJwtProperties().getSignKey().getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * 获取请求传递的token串
	 *
	 * @param auth token
	 * @return String
	 */
	public static String getToken(String auth) {
		if ((auth != null) && (auth.length() > AUTH_LENGTH)) {
			String headStr = auth.substring(0, 6).toLowerCase();
			if (headStr.compareTo(BEARER) == 0) {
				auth = auth.substring(7);
			}
			return auth;
		}
		return null;
	}

	//getBase64Security()
	/**
	 * 解析jsonWebToken
	 *
	 * @param jsonWebToken token串
	 * @return Claims
	 */
	public static Claims parseJWT(String jsonWebToken) {
		try {
			return Jwts.parser()
					.setSigningKey(Base64.getDecoder().decode(getBase64Security()))
					.parseClaimsJws(jsonWebToken).getBody();
		} catch (Exception ex) {
			return null;
		}
	}







}
