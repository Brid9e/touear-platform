package com.touear.core.cloud.feign;

import com.touear.core.cloud.http.WebUtils;
import com.touear.core.launch.constant.TokenConstant;
import com.touear.core.secure.utils.TokenUtil;
import com.touear.core.tool.utils.Func;
import com.touear.core.tool.utils.StringPool;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * feign 传递Request header
 *
 * <p>
 *     https://blog.csdn.net/u014519194/article/details/77160958
 *     http://tietang.wang/2016/02/25/hystrix/Hystrix%E5%8F%82%E6%95%B0%E8%AF%A6%E8%A7%A3/
 *     https://github.com/Netflix/Hystrix/issues/92#issuecomment-260548068
 * </p>
 *
 * @author L.cm
 */
public class TouearFeignRequestHeaderInterceptor implements RequestInterceptor {

	private final String GATEWAY_VERSION_KEY = "gatewayVersion";

	@Override
	public void apply(RequestTemplate requestTemplate) {
		try {
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = attributes.getRequest();
			requestTemplate.header(TokenConstant.HEADER, request.getHeader(TokenConstant.HEADER));
			requestTemplate.header(TokenConstant.DEVICE_TOKEN, request.getHeader(TokenConstant.DEVICE_TOKEN));
			String tenantKey = Func.toStr(Func.toStr(request.getParameter(TokenUtil.TENANT_ID)
					,request.getParameter("tenantId")), StringPool.EMPTY);
			requestTemplate.header(TokenUtil.TENANT_ID, tenantKey);
			requestTemplate.header(TokenConstant.REMOTE_ADDRESS, WebUtils.getRemoteAddress(request));
			requestTemplate.header(GATEWAY_VERSION_KEY, Func.toStr(request.getHeader(GATEWAY_VERSION_KEY),"1.0.6"));
//			requestTemplate.header(GATEWAY_VERSION_KEY, "1.0.6");

//			Enumeration<String> headerNames = request.getHeaderNames();
//			if (headerNames != null) {
//				while (headerNames.hasMoreElements()) {
//					String name = headerNames.nextElement();
//					String values = request.getHeader(name);
//					requestTemplate.header(name, values);
//				}
//			}
//			Enumeration<String> bodyNames = request.getParameterNames();
//			StringBuffer body =new StringBuffer();
//			if (bodyNames != null) {
//				while (bodyNames.hasMoreElements()) {
//					String name = bodyNames.nextElement();
//					String values = request.getParameter(name);
//					body.append(name).append("=").append(values).append("&");
//				}
//			}
//			if(body.length()!=0) {
//				body.deleteCharAt(body.length()-1);
//				requestTemplate.body(body.toString());
//			}
		}catch (Exception e) {
			requestTemplate.header(GATEWAY_VERSION_KEY, "1.0.6");
			e.printStackTrace();
		} finally {
//			requestTemplate.header(Referer.Constants.REFERER, Referer.Enum.INTERNAL.getType());
		}



		
/*		HttpHeaders headers = TouearHttpHeadersContextHolder.get();
		if (headers != null && !headers.isEmpty()) {
			headers.forEach((key, values) -> {
				values.forEach(value -> requestTemplate.header(key, value));
			});
		}*/
	}

}
