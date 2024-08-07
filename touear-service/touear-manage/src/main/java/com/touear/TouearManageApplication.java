package com.touear;

import com.touear.core.cloud.feign.EnableBerserkerFeign;
import com.touear.core.launch.constant.AppConstant;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableHystrix
@EnableScheduling
@EnableBerserkerFeign
@SpringCloudApplication
public class TouearManageApplication {


    public static void main(String[] args) {
        TouearApplication.run(AppConstant.APPLICATION_MANAGE_NAME, TouearManageApplication.class, args);
    }

}
