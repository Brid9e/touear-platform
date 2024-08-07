package com.touear.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.touear.common.constant.RedisConstants;
import com.touear.core.jwt.JwtUtil;
import com.touear.core.launch.constant.TokenConstant;
import com.touear.core.tool.api.R;
import com.touear.core.tool.utils.Func;
import com.touear.core.tool.utils.StringPool;
import com.touear.gateway.props.AuthProperties;
import com.touear.gateway.provider.AuthProvider;
import com.touear.gateway.provider.ResponseProvider;
import com.touear.gateway.util.TokenUtil;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
/**
 * 鉴权认证
 *
 * @author Chen
 */
@Slf4j
@Component
@AllArgsConstructor
@EnableConfigurationProperties({AuthProperties.class})
public class AuthFilter implements GlobalFilter, Ordered {

	private AuthProperties authProperties;
	private ObjectMapper objectMapper;
	private RedisTemplate<String, ?> redisTemplate;



//	private RedisRepo<String> strRepo;
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String path = exchange.getRequest().getURI().getPath();
		ServerHttpResponse resp = exchange.getResponse();
		if (isForbid(path)) {
			return forbidden(resp, "禁止访问");
		}
		if (isSkip(path)) {
			return chain.filter(exchange);
		}
		String auth = TokenUtil.getToken(exchange);
		if (StringUtils.isBlank(auth)) {
			return unAuth(resp, "缺失令牌,鉴权失败");
		}
		if(Objects.requireNonNull(auth).split(StringPool.SPACE).length == 1){
			auth = "bearer "+auth;
		}
		String token = JwtUtil.getToken(auth);
		Claims claims = JwtUtil.parseJWT(token);
		if (claims == null) {
			return unAuth(resp, "请求未授权");
		}else {
			String userId = Func.toStr(claims.get(TokenConstant.USER_ID));
			//"JWT_"+userId;
			String key = RedisConstants.UserConstant.LOGIN+userId;
			String jwt_uuid = Func.toStr(claims.get("uuid"));
			String loginFrom = Func.toStr(claims.get(TokenConstant.LOGIN_FROM));
			boolean hasKey = Boolean.TRUE.equals(redisTemplate.hasKey(key));
			if(Func.isBlank(jwt_uuid)){
				return unAuth(resp, "请求未授权");
			}
			if(hasKey) {
				String uuid = Func.toStr(redisTemplate.opsForHash().get(key, loginFrom)).replaceAll("\"", "");
				if(!Func.equals(jwt_uuid, uuid) && !Func.equals(loginFrom,"wechat-mina")) {
					return unAuth(resp, "已经在其他设备登录");
				}
			}else{
				return unAuth(resp, "已经登出，请重新登录");
			}
		}
		return chain.filter(exchange);
	}


	private boolean isSkip(String path) {
		AntPathMatcher pathMatcher = new AntPathMatcher();

		// 检查默认跳过的URL列表
		for (String defaultSkipUrl : AuthProvider.getDefaultSkipUrl()) {
			if (pathMatcher.match(defaultSkipUrl, path)) {
				return true;
			}
		}

		// 检查自定义跳过的URL列表
		for (String skipUrl : authProperties.getSkipUrl()) {
			if (pathMatcher.match(skipUrl, path)) {
				return true;
			}
		}

		return false;
	}







 	private boolean isForbid(String path){
		return authProperties.getForbidUrl().stream().map(url -> url.replace(AuthProvider.TARGET, AuthProvider.REPLACEMENT)).anyMatch(path::startsWith);
	}



	private Mono<Void> unAuth(ServerHttpResponse resp, String msg) {
		resp.setStatusCode(HttpStatus.UNAUTHORIZED);
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
