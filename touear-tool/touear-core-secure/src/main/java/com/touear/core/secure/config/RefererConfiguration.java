package com.touear.core.secure.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Title: RefererConfiguration
 * @Description: 接口访问来源拦截器
 * @author wangqq
 * @date 2022-04-13 09:19:57
 * @version 1.0
 */
@Order
@SpringBootConfiguration
public class RefererConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new RefererInterceptor());
    }

}
