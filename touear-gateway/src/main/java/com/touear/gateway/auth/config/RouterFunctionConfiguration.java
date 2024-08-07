package com.touear.gateway.auth.config;

import com.synjones.core.tool.utils.Func;
import com.synjones.gateway.handler.SwaggerResourceHandler;
import com.synjones.gateway.props.DomainProperties;
import com.synjones.gateway.props.RouteProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 路由配置信息
 *
 * @author Chen
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(RouteProperties.class)
public class RouterFunctionConfiguration {

	private final SwaggerResourceHandler swaggerResourceHandler;

	private DomainProperties ossProperties;

	private static final String ALLOWED_HEADERS = "x-requested-with,device-token, synjones-auth, Content-Type, Authorization, credential, X-XSRF-TOKEN, token, username, client,x_requested_with, synAccessSource";
//	private static final String ALLOWED_HEADERS = "*";
	private static final String ALLOWED_METHODS = "*";
	private static final String ALLOWED_ORIGIN = "*";
	private static final String ALLOWED_EXPOSE = "*";
	private static final String MAX_AGE = "18000L";
	
//	private AccessManager accessManager;
	
	@Bean
	public RouterFunction<?> routerFunction() {
		return RouterFunctions.route(
				RequestPredicates.GET("/swagger-resources").and(RequestPredicates.accept(MediaType.ALL)),
				swaggerResourceHandler);

	}

	
	@Bean
	public WebFilter corsFilter() {
		return (ServerWebExchange ctx, WebFilterChain chain) -> {
			ServerHttpRequest request = ctx.getRequest();
			if (CorsUtils.isCorsRequest(request)) {
				ServerHttpResponse response = ctx.getResponse();
				String origin = request.getHeaders().getOrigin();
				String address = ossProperties.getAddress();
				if (Func.isNotBlank(origin) && address.equals(origin)) {
					HttpHeaders headers = response.getHeaders();
					headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
					headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
					headers.add("Access-Control-Allow-Origin", address);
					headers.add("Access-Control-Expose-Headers", ALLOWED_EXPOSE);
					headers.add("Access-Control-Max-Age", MAX_AGE);
					headers.add("Access-Control-Allow-Credentials", "true");
				}
				if (request.getMethod() == HttpMethod.OPTIONS) {
					response.setStatusCode(HttpStatus.OK);
					return Mono.empty();
				}
			}
			return chain.filter(ctx);
		};
	}
	
/*    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        
        http
        .httpBasic().disable()
        .csrf().disable()
        .authorizeExchange()
        .pathMatchers("/").permitAll()
        // 动态权限验证
        .anyExchange().access(accessManager);
        return http.build();
    }
	*/
	
	/**
	 * 解决springboot2.0.5版本出现的 Only one connection receive subscriber allowed.
	 * 参考：https://github.com/spring-cloud/spring-cloud-gateway/issues/541
	 */
	@Bean
	public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter() {
			@Override
			public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
				return chain.filter(exchange);
			}
		};
	}

}
