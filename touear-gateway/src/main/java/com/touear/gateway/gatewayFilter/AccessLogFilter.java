package com.touear.gateway.gatewayFilter;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.touear.common.constant.RabbitConstant;
import com.touear.core.jwt.JwtUtil;
import com.touear.core.launch.constant.TokenConstant;
import com.touear.core.tool.utils.DateUtil;
import com.touear.core.tool.utils.Func;
import com.touear.gateway.provider.AuthProvider;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.touear.gateway.gatewayFilter.context.GatewayContext;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR;

/**
 *
 * @Title: AccessLogFilter.java
 * @Description: 请求日志
 * @version 1.0
 */
@Slf4j
@Component
public class AccessLogFilter implements GlobalFilter, Ordered {

	@Autowired
	private AmqpTemplate amqpTemplate;

	private static Joiner joiner = Joiner.on("");

	private final String LOGIN_PATH = "/oauth/token";
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpResponse originalResponse = exchange.getResponse();
		DataBufferFactory bufferFactory = originalResponse.bufferFactory();
		AtomicReference<String> responseDataStr = new AtomicReference<>();
		String path = exchange.getRequest().getURI().getPath();

		ServerHttpResponseDecorator response = new ServerHttpResponseDecorator(originalResponse) {
			@Override
			public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
				if (Func.equals(LOGIN_PATH,path) && (getStatusCode().equals(HttpStatus.OK) ||  getStatusCode().equals(HttpStatus.BAD_REQUEST))&& body instanceof Flux) {
					// 获取ContentType，判断是否返回JSON格式数据
					String originalResponseContentType = exchange.getAttribute(ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
					if (StringUtils.isNotBlank(originalResponseContentType) && originalResponseContentType.contains("application/json")) {
						Flux<? extends DataBuffer> fluxBody = Flux.from(body);
						//（返回数据内如果字符串过大，默认会切割）解决返回体分段传输
						return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
							List<String> list = Lists.newArrayList();
							dataBuffers.forEach(dataBuffer -> {
								try {
									byte[] content = new byte[dataBuffer.readableByteCount()];
									dataBuffer.read(content);
									DataBufferUtils.release(dataBuffer);
									list.add(new String(content, StandardCharsets.UTF_8));
								} catch (Exception e) {
									log.info("加载Response字节流异常，失败原因：{}", Throwables.getStackTraceAsString(e));
								}
							});
							String responseData = joiner.join(list);
							if(Func.equals(LOGIN_PATH,path)){
								responseData = authResponseHandle(responseData);

							}
							responseDataStr.set(responseData);

							byte[] uppedContent = new String(responseData.getBytes(), StandardCharsets.UTF_8).getBytes();
							originalResponse.getHeaders().setContentLength(uppedContent.length);
							return bufferFactory.wrap(uppedContent);
						}));
					}
				}
				return super.writeWith(body);
			}

			@Override
			public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
				return writeWith(Flux.from(body).flatMapSequential(p -> p));
			}
		};
		return chain.filter(exchange.mutate().response(response).build()).then(Mono.fromRunnable(() -> {
			sendLog(exchange, responseDataStr,null);
		}));
	}

	private String authResponseHandle(String responseData) {
		try {
			JSONObject jsonObject = JSONObject.parseObject(responseData);
			if(Func.equals(Func.toStr(jsonObject.get("message")),"Bad credentials")){
				jsonObject.put("message", "用户名或密码错误");
				responseData = jsonObject.toJSONString();
			}
		} catch (Exception e) {
			log.info("返回数据处理转化失败，异常信息={}",e.getMessage());
			return responseData;
		}
		return responseData;
	}


	/**
	 * 返回数据处理
	 *
	 * @param responseData
	 * @return
	 */
	private String responseHandle(String responseData) {
		String responseResultJson = null;
		try {
			JSONObject jsonObject = JSONObject.parseObject(responseData);
			Object data = jsonObject.get("data");
			if(data != null && data instanceof List){
				List list = (List) data;
				if(list.size() > 100){
					jsonObject.put("data",list.subList(0,100));
				}
			}
			responseResultJson = jsonObject.toJSONString();
		} catch (Exception e) {
			log.info("返回数据处理转化失败，异常信息={}",e.getMessage());
			return responseData;
		}
		return responseResultJson;
	}
	public void sendLog(ServerWebExchange exchange, AtomicReference<String> responseDataStr, Exception ex) {
		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse response = exchange.getResponse();
		try {
			int httpStatus = response.getStatusCode().value();
			String requestPath = request.getURI().getPath();
			String method = request.getMethodValue();
			Map<String, String> headers = request.getHeaders().toSingleValueMap();
			Map data = Maps.newHashMap();
			GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
			if (gatewayContext != null) {
				data = gatewayContext.getAllRequestData().toSingleValueMap();
			}

			String ip = getRemoteAddress(exchange);
			String userAgent = headers.get(HttpHeaders.USER_AGENT);
			Date requestTime = exchange.getAttribute("requestTime");
			String error = null;
			if (ex != null) {
				error = ex.getMessage();
			}
			Map<String, Object> map = Maps.newHashMap();

			String headerToken = exchange.getRequest().getHeaders().getFirst(AuthProvider.AUTH_KEY);
			String paramToken = exchange.getRequest().getQueryParams().getFirst(AuthProvider.AUTH_KEY);
			String auth = StringUtils.isBlank(headerToken) ? paramToken : headerToken;
			if(!Func.isBlank(auth)){
				String token = JwtUtil.getToken(auth);
				Claims claims = JwtUtil.parseJWT(token);
				if (claims != null) {
					String userId = Func.toStr(claims.get(TokenConstant.USER_ID));
					map.put("accountId",userId);
				}
			}

			Date responseTime = new Date();
			map.put("httpStatus", httpStatus);
			map.put("path", requestPath);
			map.put("params", JSONObject.toJSON(data));
			map.put("ip", ip);
			map.put("method", method);
			map.put("userAgent", userAgent);
			map.put("responseTime", DateUtil.formatDateTime(responseTime));
			map.put("useTime", responseTime.getTime() - requestTime.getTime());
			map.put("error", error);
			map.put("responseData",responseDataStr.get());
			amqpTemplate.convertAndSend(RabbitConstant.Log.LOG_ACCESS_EXCHANGE, RabbitConstant.Log.PUBLICKEY, map);
		} catch (Exception e) {
			log.error("access logs save error:{}", e);
		}

	}

	public static String getRemoteAddress(ServerWebExchange exchange) {
		ServerHttpRequest request = exchange.getRequest();
		Map<String, String> headers = request.getHeaders().toSingleValueMap();
		String unknown = "unknown";
		String ip = headers.get("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
			ip = headers.get("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
			ip = headers.get("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
			ip = headers.get("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
			ip = headers.get("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
			ip = headers.get("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddress().getAddress().getHostAddress();
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ip != null && ip.length() > 0) {
			String[] ips = ip.split(",");
			if (ips.length > 0) {
				ip = ips[0];
			}
		}
		return ip;
	}

	@Override
	public int getOrder() {
		return -100;
	}

}
