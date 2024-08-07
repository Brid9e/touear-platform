package com.touear.gateway.config;


import com.touear.gateway.filter.LogFilter;
import com.touear.gateway.filter.RequestTimeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class GatewayConfig {



    @Bean
    @Order(-2)
    public LogFilter logFilter(){
        return new LogFilter();
    }
    @Bean
    public RequestTimeFilter requestTimeFilter(){
        return new RequestTimeFilter();
    }

//    @Bean
//    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
//        // @formatter:off
//        return builder.routes()
//                .route(r -> r.path("/order/**")
//                        .filters(f -> f.filter(new RequestTimeFilter())
//                                .addResponseHeader("X-Response-Default-Foo", "Default-Bar"))
//                        .uri("http://httpbin.org:80/get")
//                )
//                .build();
//        // @formatter:on
//    }




}
