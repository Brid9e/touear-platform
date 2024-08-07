package com.touear.gateway.util;

import com.touear.core.tool.utils.Func;
import com.touear.gateway.gatewayFilter.context.GatewayContext;
import com.touear.gateway.provider.AuthProvider;
import org.springframework.http.HttpCookie;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

/**
 * @Title: TokenUtil
 * @Description: 获取请求中的token参数
 * @author wangqq
 * @date 2022-12-23 13:54:08
 * @version 1.0
 */
public class TokenUtil {

    /**
     * 获取请求中的token参数，优先级：header > param > cookie
     *
     * @param exchange
     * @return {@link String}
     * @author wangqq
     * @date 2022-12-23 13:57:52
     */
    public static String getToken(ServerWebExchange exchange) {
        String touearAuth = exchange.getRequest().getHeaders().getFirst(AuthProvider.AUTH_KEY);
        if (Func.isNotBlank(touearAuth)) return touearAuth;
        touearAuth = exchange.getRequest().getQueryParams().getFirst(AuthProvider.AUTH_KEY);
        if (Func.isNotBlank(touearAuth)) return touearAuth;
        HttpCookie httpCookie = exchange.getRequest().getCookies().getFirst(AuthProvider.AUTH_KEY);
        if (httpCookie != null) {
            touearAuth = httpCookie.getValue();
            if (Func.isNotBlank(touearAuth)) return Func.unescape(touearAuth);
        }
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if (gatewayContext != null) {
            Map<String, String> params = gatewayContext.getAllRequestData().toSingleValueMap();
            touearAuth = params.get(AuthProvider.AUTH_KEY);
            if (Func.isNotBlank(touearAuth)) return Func.unescape(touearAuth);
        }
        return null;
    }

}
