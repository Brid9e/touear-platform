package com.touear.core.tool.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.function.Predicate;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * Miscellaneous utilities for web applications.
 *
 * @author L.cm
 */
public class WebUtil extends org.springframework.web.util.WebUtils {

	public static final String USER_AGENT_HEADER = "user-agent";

	/**
	 * 获取 HttpServletRequest
	 *
	 * @return {HttpServletRequest}
	 */
	public static HttpServletRequest getRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		return (requestAttributes == null) ? null : ((ServletRequestAttributes) requestAttributes).getRequest();
	}
	/**
	 * 获取 HttpServletResponse
	 *
	 * @return {HttpServletResponse}
	 */
	public static HttpServletResponse getHttpServletResponse() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		return (requestAttributes == null) ? null : ((ServletRequestAttributes) requestAttributes).getResponse();
	}

	/**
	 * 获取ip
	 *
	 * @return {String}
	 */
	public static String getIP() {
		return getIP(WebUtil.getRequest());
	}

	private static final String[] IP_HEADER_NAMES = new String[]{
		"X-Requested-For",
		"Proxy-Client-IP",
		"WL-Proxy-Client-IP",
		"HTTP_CLIENT_IP",
		"HTTP_X_FORWARDED_FOR"
	};

	private static final Predicate<String> IP_PREDICATE = (ip) -> StringUtil.isBlank(ip) || StringPool.UNKNOWN.equalsIgnoreCase(ip);

	/**
	 * 获取ip
	 *
	 * @param request HttpServletRequest
	 * @return {String}
	 */
	@Nullable
	public static String getIP(@Nullable HttpServletRequest request) {
		if (request == null) {
			return StringPool.EMPTY;
		}
		String ip = null;
		for (String ipHeader : IP_HEADER_NAMES) {
			ip = request.getHeader(ipHeader);
			if (IP_PREDICATE.test(ip)) {
				break;
			}
		}
		if (IP_PREDICATE.test(ip)) {
			ip = request.getRemoteAddr();
		}
		return StringUtil.isBlank(ip) ? null : StringUtil.splitTrim(ip, StringPool.COMMA)[0];
	}

	/**
	 * 获取 request 请求体
	 *
	 * @param servletInputStream servletInputStream
	 * @return body
	 */
	public static String getRequestBody(ServletInputStream servletInputStream) {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(servletInputStream, StandardCharsets.UTF_8));
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (servletInputStream != null) {
				try {
					servletInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 获取 request 请求内容
	 *
	 * @param request request
	 * @return {String}
	 */
	public static String getRequestContent(HttpServletRequest request) {
		try {
			String queryString = request.getQueryString();
			if (StringUtil.isNotBlank(queryString)) {
				return new String(queryString.getBytes(Charsets.ISO_8859_1), Charsets.UTF_8).replaceAll("&amp;", "&").replaceAll("%22", "\"");
			}
			String charEncoding = request.getCharacterEncoding();
			if (charEncoding == null) {
				charEncoding = StringPool.UTF_8;
			}
			byte[] buffer = getRequestBody(request.getInputStream()).getBytes();
			String str = new String(buffer, charEncoding).trim();
			if (StringUtil.isBlank(str)) {
				StringBuilder sb = new StringBuilder();
				Enumeration<String> parameterNames = request.getParameterNames();
				while (parameterNames.hasMoreElements()) {
					String key = parameterNames.nextElement();
					String value = request.getParameter(key);
					StringUtil.appendBuilder(sb, key, "=", value, "&");
				}
				str = StringUtil.removeSuffix(sb.toString(), "&");
			}
			return str.replaceAll("&amp;", "&");
		} catch (Exception ex) {
			ex.printStackTrace();
			return StringPool.EMPTY;
		}
	}
}

