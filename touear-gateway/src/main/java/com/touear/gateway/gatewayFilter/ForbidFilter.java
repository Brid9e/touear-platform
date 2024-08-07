package com.touear.gateway.gatewayFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.touear.gateway.props.AuthProperties;
import com.touear.gateway.provider.AuthProvider;
import com.touear.gateway.provider.ResponseProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 禁用访问
 *
 * @author Chen
 */
@Slf4j
@Component
@AllArgsConstructor
@EnableConfigurationProperties({AuthProperties.class})
public class ForbidFilter implements GlobalFilter, Ordered {

	private ObjectMapper objectMapper;

	private static final List<String> defaultForbidUrl = new ArrayList<>();
	private static final List<String> defaultForbidService = new ArrayList<>();

	static {
		defaultForbidUrl.add("/v2/**");
	}


	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
		ServerHttpResponse resp = exchange.getResponse();
		if (route != null) {
			String serviceName = route.getUri().getHost();
			if(defaultForbidService.contains(serviceName)){
				return forbidden(resp, "禁止访问");
			}
		}

		String path = exchange.getRequest().getURI().getPath();
		if (isForbid(path)) {
			return forbidden(resp, "禁止访问");
		}

		return chain.filter(exchange);
	}



 	private boolean isForbid(String path){
		return defaultForbidUrl.stream().map(url -> url.replace(AuthProvider.TARGET, AuthProvider.REPLACEMENT)).anyMatch(path::startsWith);
	}




	private Mono<Void> forbidden(ServerHttpResponse resp, String msg) {
		resp.setStatusCode(HttpStatus.FORBIDDEN);
		resp.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
		String result = "";
		try {
			result = objectMapper.writeValueAsString(ResponseProvider.unAuth(msg));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		DataBuffer buffer = resp.bufferFactory().wrap(result.getBytes(StandardCharsets.UTF_8));
		return resp.writeWith(Flux.just(buffer));
	}
	@Override
	public int getOrder() {
		return -100;
	}

}
