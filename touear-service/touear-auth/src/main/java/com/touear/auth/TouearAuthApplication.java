package com.touear.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.touear.auth.mapper")
public class TouearAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(TouearAuthApplication.class, args);
    }

}
