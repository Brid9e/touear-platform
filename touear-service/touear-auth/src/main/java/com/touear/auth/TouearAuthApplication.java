package com.touear.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.touear.auth")
@MapperScan("com.touear.auth.mapper")
public class TouearAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(TouearAuthApplication.class, args);
    }

}
