package com.touear.gateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.touear.core.tool.api.R;
import com.touear.gateway.provider.ResponseProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 
 * @Title: JsonAccessDeniedHandler.java
 * @Description: 网关权限异常处理,记录日志
 * @author chenl
 * @date 2020-06-15 13:48:42
 * @version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class JsonAccessDeniedHandler implements ServerAccessDeniedHandler {
	

    private ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException e) {
    	ServerHttpResponse resp = exchange.getResponse();
        R<?> resultBody = OpenGlobalExceptionHandler.resolveException(e);
       
        resp.setStatusCode(HttpStatus.UNAUTHORIZED);
		resp.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
		String result = "";
		try {
			result = objectMapper.writeValueAsString(ResponseProvider.unAuth(resultBody.getCode(),resultBody.getMsg()));

		} catch (JsonProcessingException e1) {
			log.error(e.getMessage(), e1);
		}
		DataBuffer buffer = resp.bufferFactory().wrap(result.getBytes(StandardCharsets.UTF_8));
		return resp.writeWith(Flux.just(buffer));
    	
        
    }
    
    
    
}
