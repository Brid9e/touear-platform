package com.touear.gateway.exception;

import com.alibaba.fastjson.JSONObject;
import com.synjones.core.tool.api.R;
import com.synjones.core.tool.exception.OpenSignatureException;
import com.synjones.gateway.service.AccessLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

@Slf4j
@Component
@AllArgsConstructor
public class JsonSignatureDeniedHandler   {
  
	private AccessLogService accessLogService;

    public Mono<Void> handle(ServerWebExchange exchange, OpenSignatureException e) {
    	R<?> resultBody = OpenGlobalExceptionHandler.resolveException(e);
        return Mono.defer(() -> {
            return Mono.just(exchange.getResponse());
        }).flatMap((response) -> {
//            response.setStatusCode(HttpStatus.valueOf(resultBody.getCode()));
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
            DataBufferFactory dataBufferFactory = response.bufferFactory();
            DataBuffer buffer = dataBufferFactory.wrap(JSONObject.toJSONString(resultBody).getBytes(Charset.defaultCharset()));
            // 保存日志
            accessLogService.sendLog(exchange, e);
            return response.writeWith(Mono.just(buffer)).doOnError((error) -> {
                DataBufferUtils.release(buffer);
            });
        });
    }
}
