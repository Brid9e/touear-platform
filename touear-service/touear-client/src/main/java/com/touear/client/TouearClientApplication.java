package com.touear.client;

import com.touear.TouearApplication;
import com.touear.core.cloud.feign.EnableTouearFeign;
import com.touear.core.launch.constant.AppConstant;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableHystrix
@EnableScheduling
@EnableTouearFeign
@SpringCloudApplication
public class TouearClientApplication {
    public static void main(String[] args) {
        TouearApplication.run(AppConstant.APPLICATION_CLIENT_NAME, TouearClientApplication.class, args);
    }

}
