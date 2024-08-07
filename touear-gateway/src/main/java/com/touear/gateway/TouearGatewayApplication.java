package com.touear.gateway;

import com.touear.TouearApplication;
import com.touear.core.cloud.feign.EnableBerserkerFeign;
import com.touear.core.launch.constant.AppConstant;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableHystrix
@EnableScheduling
@EnableBerserkerFeign
@SpringCloudApplication
public class TouearGatewayApplication {

    public static void main(String[] args) {
        TouearApplication.run(AppConstant.APPLICATION_GATEWAY_NAME, TouearGatewayApplication.class, args);
    }

}
