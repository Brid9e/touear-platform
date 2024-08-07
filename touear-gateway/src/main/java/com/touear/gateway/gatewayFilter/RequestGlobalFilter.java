package com.touear.gateway.gatewayFilter;

import com.touear.core.tool.utils.Func;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

/**
 * <p>
 * 全局拦截器，作用所有的微服务
 * <p>
 * 1. 对请求头中参数进行处理 from 参数进行清洗 2. 重写StripPrefix = 1,支持全局
 *
 * @author lengleng
 */
@Component
@Slf4j
public class RequestGlobalFilter implements GlobalFilter, Ordered {

	private final String LOGIN_PATH = "/berserker-auth/oauth/token";
	/**
	 * Process the Web request and (optionally) delegate to the next
	 * {@code WebFilter} through the given {@link GatewayFilterChain}.
	 *
	 * @param exchange
	 *            the current server exchange
	 * @param chain
	 *            provides a way to delegate to the next filter
	 * @return {@code Mono<Void>} to indicate when request processing is complete
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// 1. 清洗请求头中from 参数
		ServerHttpRequest request = exchange.getRequest().mutate()
				.headers(httpHeaders -> httpHeaders.remove("X"))
				.headers(httpHeaders -> httpHeaders.add("gatewayVersion","1.0.6"))
				.headers(httpHeaders -> {
					if(!Func.equals(exchange.getRequest().getPath().value(),LOGIN_PATH)){
						httpHeaders.remove("Authorization");
					}
				})
				.build();

		// 2. 重写StripPrefix
		addOriginalRequestUrl(exchange, request.getURI());
		String rawPath = request.getURI().getRawPath();
		String newPath = "/" + Arrays.stream(StringUtils.tokenizeToStringArray(rawPath, "/")).skip(1L)
				.collect(Collectors.joining("/"));
		ServerHttpRequest newRequest = request.mutate().path(newPath).build();
		exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newRequest.getURI());
		// 添加请求时间
		exchange.getAttributes().put("requestTime", new Date());
		return chain.filter(exchange.mutate().request(newRequest.mutate().build()).build());

	}


	@Override
	public int getOrder() {
		return -1000;
	}

}
