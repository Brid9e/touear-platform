package com.touear.core.secure.utils;

import java.security.Key;
import java.util.*;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.touear.core.jwt.JwtUtil;
import com.touear.core.launch.constant.TokenConstant;
import com.touear.core.secure.User;
import com.touear.core.secure.provider.IClientDetailsService;
import com.touear.core.tool.utils.Func;
import com.touear.core.tool.utils.StringPool;
import com.touear.core.tool.utils.WebUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Secure工具类
 *
 * @author Chen
 */
public class SecureUtil {
	
	private static final String USER_REQUEST_ATTR = "_USER_REQUEST_ATTR_";

	private final static String HEADER = TokenConstant.HEADER;
	private final static String DEVICE_TOKEN = TokenConstant.DEVICE_TOKEN;

	public final static String IDENTITY_TYPE = TokenConstant.IDENTITY_TYPE;
	private final static String BEARER = TokenConstant.BEARER;
	private final static String ACCOUNT = TokenConstant.ACCOUNT;
	private final static String USER_ID = TokenConstant.USER_ID;
	private final static String CARE_ACCOUNT = TokenConstant.CARD_ACCOUNRT;
	private final static String ROLE_ID = TokenConstant.ROLE_ID;
	private final static String USER_NAME = TokenConstant.USER_NAME;
	private final static String ROLE_NAME = TokenConstant.ROLE_NAME;
	private final static String SNO = TokenConstant.SNO;
	private final static String CLIENT_ID = TokenConstant.CLIENT_ID;
	private final static Integer AUTH_LENGTH = TokenConstant.AUTH_LENGTH;
	private final static String USER_GROUP = TokenConstant.USER_GROUP;
	private final static String USER_MERCACC = TokenConstant.USER_MERCACC;
	private final static String LOGIN_FROM = TokenConstant.LOGIN_FROM;
	private final static String TENANT_ID = TokenConstant.TENANT_ID;
	private final static String SUBAREA_ID = TokenConstant.SUBAREA_ID;
	private final static String AGENCY_ID = TokenConstant.AGENCY_ID;
	private final static String FLAG = TokenConstant.FLAG;
	
//	private static String BASE64_SECURITY = Base64.getEncoder().encodeToString(TokenConstant.SIGN_KEY.getBytes(Charsets.UTF_8));

	private static IClientDetailsService clientDetailsService;


	/**
	 * 获取用户信息
	 *
	 * @return User
	 */
	public static User getUser() {
		HttpServletRequest request = WebUtil.getRequest();
		if (request == null) {
			return null;
		}
		// 优先从 request 中获取
		Object user = request.getAttribute(USER_REQUEST_ATTR);
		if (user == null) {
			user = getUser(request);
			if (user != null) {
				// 设置到 request 中
				request.setAttribute(USER_REQUEST_ATTR, user);
			}
		}
		return (User) user;
	}

	/**
	 * 获取用户信息
	 *
	 * @param request request
	 * @return User
	 */
	public static User getUser(HttpServletRequest request) {
		Claims claims = getClaims(request);
		if (claims == null) {
			return null;
		}
		String clientId = Func.toStr(claims.get(SecureUtil.CLIENT_ID));
		Long userId = Func.toLong(claims.get(SecureUtil.USER_ID));
		String roleId = Func.toStr(claims.get(SecureUtil.ROLE_ID));
		String account = Func.toStr(claims.get(SecureUtil.ACCOUNT));
		String roleName = Func.toStr(claims.get(SecureUtil.ROLE_NAME));
		String userName = Func.toStr(claims.get(SecureUtil.USER_NAME));
		String sno = Func.toStr(claims.get(SecureUtil.SNO));
		String cardAccount = Func.toStr(claims.get(SecureUtil.CARE_ACCOUNT));
		String tenantId = Func.toStr(claims.get(SecureUtil.TENANT_ID));

		Long subareaId = Func.toLong(claims.get(SecureUtil.SUBAREA_ID));
		Long agencyId = Func.toLong(claims.get(SecureUtil.AGENCY_ID));

		List<Long> userGroup = Func.toLongList(Func.toStr(claims.get(SecureUtil.USER_GROUP)));
		List<String> userMercacc = Func.toStrList(Func.toStr(claims.get(SecureUtil.USER_MERCACC)));
		//BeanUtil
		String loginFrom = Func.toStr(claims.get(SecureUtil.LOGIN_FROM));
		String flag = Func.toStr(claims.get(SecureUtil.FLAG),"0");
		String identityType = Func.toStr(claims.get(SecureUtil.IDENTITY_TYPE));
		String isOperator = "0";
		if(Func.isNotBlank(identityType) && Arrays.asList("system", "subarea", "agency", "tenant").contains(identityType)){
			isOperator = "1";
		}
		User user = new User();

		user.setId(userId);
		user.setAccount(account);
		user.setName(userName);
		user.setLoginFrom(loginFrom);
		return user;
	}

	public static String getIdentityType() {
		HttpServletRequest request = WebUtil.getRequest();
		if(request != null){
//			String token = request.getHeader(SecureUtil.HEADER);
//			if(Func.isNotBlank(token)){
				Claims claims = getClaims(request);
				if(claims != null){
					return Func.toStr(claims.get(SecureUtil.IDENTITY_TYPE));
				}
//			}
		}
		return null;
	}
	/**
	 * 获取用户id
	 *
	 * @return userId
	 */
	public static Long getUserId() {
		User user = getUser();
		return (null == user) ? -1 : user.getId();
	}


	/**
	 * 获取用户id
	 *
	 * @param request request
	 * @return userId
	 */
	public static Long getUserId(HttpServletRequest request) {
		User user = getUser(request);
		return (null == user) ? -1 : user.getId();
	}


	/**
	 * 获取用户账号
	 *
	 * @return userAccount
	 */
	public static String getUserAccount() {
		User user = getUser();
		return (null == user) ? StringPool.EMPTY : user.getAccount();
	}

	/**
	 * 获取用户账号
	 *
	 * @param request request
	 * @return userAccount
	 */
	public static String getUserAccount(HttpServletRequest request) {
		User user = getUser(request);
		return (null == user) ? StringPool.EMPTY : user.getAccount();
	}

	/**
	 * 获取用户名
	 *
	 * @return userName
	 */
	public static String getUserName() {
		User user = getUser();
		return (null == user) ? StringPool.EMPTY : user.getName();
	}

	/**
	 * 获取用户名
	 *
	 * @param request request
	 * @return userName
	 */
	public static String getUserName(HttpServletRequest request) {
		User user = getUser(request);
		return (null == user) ? StringPool.EMPTY : user.getName();
	}




	/**
	 * 获取Claims
	 *
	 * @param request request
	 * @return Claims
	 */
	public static Claims getClaims(HttpServletRequest request) {
		
		String auth = request.getHeader(SecureUtil.HEADER);
		if(auth == null) {
			auth = request.getParameter(SecureUtil.HEADER);
		}
		if(auth == null) {
			Cookie[] cookies = request.getCookies();
			if(cookies != null) {
		        for (int i = 0; i < cookies.length; i++) {
		        	if(Func.equals(SecureUtil.HEADER, cookies[i].getName())) {
		        		auth = Func.unescape(cookies[i].getValue());
		        		break;
		        	}
		        }
			}
		}
		if ((auth != null) && (auth.length() > AUTH_LENGTH)) {
			String headStr = auth.toLowerCase();
			if(auth.split(" ").length == 2) {
				headStr = auth.substring(0, 6).toLowerCase();
			}
			if (headStr.compareTo(SecureUtil.BEARER) == 0) {
				auth = auth.substring(7);
			}
			return SecureUtil.parseJWT(auth);
		}
		return null;
	}

	public static String createJWT(Map<String, Object> user, String audience, String issuer, boolean isExpire) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		//生成签名密钥
		byte[] apiKeySecretBytes = Base64.getDecoder().decode(JwtUtil.getBase64Security());
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		//添加构成JWT的类
		JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JsonWebToken")
			.setIssuer(issuer)
			.setAudience(audience)
			.signWith(signatureAlgorithm, signingKey);

		//设置JWT参数
		user.forEach(builder::claim);

		//添加Token过期时间
		if (isExpire) {
			long expMillis = nowMillis + getExpire();
			Date exp = new Date(expMillis);
			builder.setExpiration(exp).setNotBefore(now);
		}

		//生成JWT
		return builder.compact();
	}
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
	 * 解析jsonWebToken
	 *
	 * @param jsonWebToken jsonWebToken
	 * @return Claims
	 */
	public static Claims parseJWT(String jsonWebToken) {
		return JwtUtil.parseJWT(jsonWebToken);
	}


	public static String getDeviceId() {
		HttpServletRequest request = WebUtil.getRequest();
		if(request != null){
			String token = request.getHeader(SecureUtil.DEVICE_TOKEN);
			if(Func.isNotBlank(token)){
				Claims claims = parseJWT(token);
				if(claims != null){
					return Func.toStr(claims.get(SecureUtil.CLIENT_ID));
				}
			}
		}
		return null;

	}

}
