package com.touear;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.touear.manage.mapper")
public class TouearManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(TouearManageApplication.class, args);
    }

}
