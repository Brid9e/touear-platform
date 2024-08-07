//package com.touear.core.secure.interceptor;
//
//import java.io.IOException;
//import java.util.Objects;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.http.MediaType;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import com.touear.core.secure.utils.SecureUtil;
//import com.touear.core.tool.api.R;
//import com.touear.core.tool.api.ResultCode;
//import com.touear.core.tool.constant.TouearConstant;
//import com.touear.core.tool.jackson.JsonUtil;
//import com.touear.core.tool.utils.WebUtil;
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * jwt拦截器校验
// *
// * @author Chen
// */
//@Slf4j
//@AllArgsConstructor
//public class SecureInterceptor extends HandlerInterceptorAdapter {
//
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//		if (null != SecureUtil.getUser()) {
//			return true;
//		} else {
//			log.warn("签名认证失败，请求接口：{}，请求IP：{}，请求参数：{}", request.getRequestURI(), WebUtil.getIP(request), JsonUtil.toJson(request.getParameterMap()));
//			R result = R.fail(ResultCode.UN_AUTHORIZED);
//			response.setCharacterEncoding(TouearConstant.UTF_8);
//			response.setHeader(TouearConstant.CONTENT_TYPE_NAME, MediaType.APPLICATION_JSON_UTF8_VALUE);
//			response.setStatus(HttpServletResponse.SC_OK);
//			try {
//				response.getWriter().write(Objects.requireNonNull(JsonUtil.toJson(result)));
//			} catch (IOException ex) {
//				log.error(ex.getMessage());
//			}
//			return false;
//		}
//	}
//}
