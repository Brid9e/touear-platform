package com.touear.core.secure.utils;

import com.touear.core.jwt.JwtUtil;
import com.touear.core.launch.constant.TokenConstant;
import com.touear.core.tool.utils.StringPool;
import com.touear.core.tool.utils.WebUtil;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;


/**
 * 认证工具类
 *
 * @author Chen
 */
public class TokenUtil {

	public final static String AVATAR = TokenConstant.AVATAR;
	public final static String ACCOUNT = TokenConstant.ACCOUNT;
	public final static String USER_ID = TokenConstant.USER_ID;
	public final static String ROLE_ID = TokenConstant.ROLE_ID;
	public final static String USER_NAME = TokenConstant.USER_NAME;
	public final static String NICK_NAME = TokenConstant.NICK_NAME;
	public final static String ROLE_NAME = TokenConstant.ROLE_NAME;
	public final static String CLIENT_ID = TokenConstant.CLIENT_ID;
	public final static String LICENSE = TokenConstant.LICENSE;
	public final static String NAME = TokenConstant.NAME;
	public final static String CARD_ACCOUNRT = TokenConstant.CARD_ACCOUNRT;
	public final static String USER_GROUP = TokenConstant.USER_GROUP;
	public final static String USER_MERCACC = TokenConstant.USER_MERCACC;
	public final static String UUID = "uuid";
	public final static String SNO = TokenConstant.SNO;
	public final static String ID_NUMBER = "id_number";
	public final static String LICENSE_NAME = TokenConstant.LICENSE_NAME;
	public final static String TENANT_ID = TokenConstant.TENANT_ID;
	public final static String IDENTITY_TYPE = TokenConstant.IDENTITY_TYPE;


	public final static String CAPTCHA_HEADER_KEY = "captcha_header_key";

	public final static String CAPTCHA_HEADER_CODE = "captcha_header_code";

	public final static String ERROR_TIMES = "error_times";

	public final static String LOGIN_TYPE_KEY = "logintype";

	public final static String IS_FIRST_LOGIN = "is_first_login";

	/**
	 * flag【0】 1需要发验证码（无手机号去完善手机号信息） 0不用 【1】是否首次登录 【2】1强制修改密码2提醒修改密码0无需修改密码 【3】是否为设备首次登录
	 */
	public final static String FLAG = TokenConstant.FLAG;

	public final static String LOGIN_FROM_KEY = TokenConstant.LOGIN_FROM;

	public final static String LOGIN_TOKEN = "token";


	public final static String USER_TYPE = TokenConstant.USER_TYPE;

	public final static String DEFAULT_LOGIN_FROM_CODE = "app";
	
	public final static String DEFAULT_LOGIN_PASSWORD = "888888";

	public final static String LOGIN_PASSWORD = "password";

	public final static String USER_NOT_FOUND = "用户名或密码错误";
	public final static String HEADER_KEY = "Authorization";
	public final static String HEADER_PREFIX = "Basic ";
	public final static String DEFAULT_AVATAR = "d";
	public final static String USER = "user";

	/**
	 * 解码
	 */
	@SneakyThrows
	public static String[] extractAndDecodeHeader() {
		String header = WebUtil.getRequest().getHeader(TokenUtil.HEADER_KEY);
		if (header == null || !header.startsWith(TokenUtil.HEADER_PREFIX)) {
			throw new Exception("请求头中无client信息");
		}

		byte[] base64Token = header.substring(6).getBytes("UTF-8");

		byte[] decoded;
		try {
			decoded = Base64.getDecoder().decode(base64Token);
		} catch (IllegalArgumentException var7) {
			throw new Exception("Failed to decode basic authentication token");
		}

		String token = new String(decoded, "UTF-8");
		int index = token.indexOf(StringPool.COLON);
		if (index == -1) {
			throw new Exception("Invalid basic authentication token");
		} else {
			return new String[] { token.substring(0, index), token.substring(index + 1) };
		}
	}

	/**
	 * 获取请求头中的客户端id
	 */
	public static String getClientIdFromHeader() {
		String[] tokens = extractAndDecodeHeader();
		assert tokens.length == 2;
		return tokens[0];
	}

	/**
	 * 获取token过期时间(次日凌晨3点)
	 *
	 * @return expire
	 */
	public static int getTokenValiditySecond() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 1);
		cal.set(Calendar.HOUR_OF_DAY, 3);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return (int) (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
	}

	/**
	 * 获取refreshToken过期时间
	 *
	 * @return expire
	 */
	public static int getRefreshTokenValiditySeconds() {
		return 60 * 60 * 24 * 15;
	}

//	private static String BASE64_SECURITY = Base64.getEncoder().encodeToString(TokenConstant.SIGN_KEY.getBytes(Charsets.UTF_8));

	/**
	 * 获取过期时间(次日凌晨3点)
	 *
	 * @return expire
	 */
	public static long getExpire() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 1);
		cal.set(Calendar.HOUR_OF_DAY, 3);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis() - System.currentTimeMillis();
	}

	/**
	 * 
	 * 
	 * @param accountId
	 * @param account
	 * @param name
	 * @param sno
	 * @param loginType
	 * @param loginFrom
	 * @param uuid
	 * @param audience
	 * @param issuer
	 * @param isExpire
	 * @return
	 * @author chenl
	 * @date 2020-12-17 10:51:00
	 */
	public static String createJWT(Long accountId, String account, String name, String sno, String loginType,
			String loginFrom, String uuid, String audience, String issuer, boolean isExpire) {

		Map<String, Object> user = new HashMap<String, Object>();
		user.put(USER_ID, accountId);
		user.put(ACCOUNT, account);
		user.put(USER_NAME, name);
		user.put(SNO, sno);
		user.put(LOGIN_TYPE_KEY, loginType);
		user.put(LOGIN_FROM_KEY, loginFrom);
		user.put(UUID, uuid);
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		// 5分钟
		long nowMillis = System.currentTimeMillis() - 500000;
		Date now = new Date(nowMillis);

		// 生成签名密钥
		byte[] apiKeySecretBytes = Base64.getDecoder().decode(JwtUtil.getBase64Security());
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		// 添加构成JWT的类
		JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JsonWebToken").setIssuer(issuer)
				.setAudience(audience).signWith(signatureAlgorithm, signingKey);

		// 设置JWT参数
		user.forEach(builder::claim);

		// 添加Token过期时间
		if (isExpire) {
			long expMillis = nowMillis + getExpire();
			Date exp = new Date(expMillis);
			builder.setExpiration(exp).setNotBefore(now);
		}

		// 生成JWT
		return builder.compact();
	}
	/**
	 *
	 *
	 * @param accountId
	 * @param account
	 * @param name
	 * @param sno
	 * @param loginType
	 * @param loginFrom
	 * @param uuid
	 * @param audience
	 * @param issuer
	 * @param isExpire
	 * @return
	 * @author chenl
	 * @date 2020-12-17 10:51:00
	 */
	public static String createJWT(Long accountId, String account, String name, String sno, String loginType,
								   String loginFrom, String uuid,
								   String audience, String issuer, boolean isExpire,String clientId) {

		Map<String, Object> user = new HashMap<String, Object>();
		user.put(USER_ID, accountId);
		user.put(ACCOUNT, account);
		user.put(USER_NAME, name);
		user.put(SNO, sno);
		user.put(LOGIN_TYPE_KEY, loginType);
		user.put(LOGIN_FROM_KEY, loginFrom);
		user.put(UUID, uuid);
		user.put(CLIENT_ID, clientId);
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		// 5分钟
		long nowMillis = System.currentTimeMillis() - 500000;
		Date now = new Date(nowMillis);

		// 生成签名密钥
		byte[] apiKeySecretBytes = Base64.getDecoder().decode(JwtUtil.getBase64Security());
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		// 添加构成JWT的类
		JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JsonWebToken").setIssuer(issuer)
				.setAudience(audience).signWith(signatureAlgorithm, signingKey);

		// 设置JWT参数
		user.forEach(builder::claim);

		// 添加Token过期时间
		if (isExpire) {
			long expMillis = nowMillis + getExpire();
			Date exp = new Date(expMillis);
			builder.setExpiration(exp).setNotBefore(now);
		}

		// 生成JWT
		return builder.compact();
	}



	public static String createJWT(String tenantId,Long accountId, String account, String name, String sno, String loginType,
								   String loginFrom, String uuid, String audience, String issuer, boolean isExpire) {

		Map<String, Object> user = new HashMap<String, Object>();
		user.put(USER_ID, accountId);
		user.put(ACCOUNT, account);
		user.put(USER_NAME, name);
		user.put(SNO, sno);
		user.put(LOGIN_TYPE_KEY, loginType);
		user.put(LOGIN_FROM_KEY, loginFrom);
		user.put(UUID, uuid);
		user.put(TENANT_ID,tenantId);
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		// 5分钟
		long nowMillis = System.currentTimeMillis() - 500000;
		Date now = new Date(nowMillis);

		// 生成签名密钥
		byte[] apiKeySecretBytes = Base64.getDecoder().decode(JwtUtil.getBase64Security());
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		// 添加构成JWT的类
		JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JsonWebToken").setIssuer(issuer)
				.setAudience(audience).signWith(signatureAlgorithm, signingKey);

		// 设置JWT参数
		user.forEach(builder::claim);

		// 添加Token过期时间
		if (isExpire) {
			long expMillis = nowMillis + getExpire();
			Date exp = new Date(expMillis);
			builder.setExpiration(exp).setNotBefore(now);
		}

		// 生成JWT
		return builder.compact();
	}
}
