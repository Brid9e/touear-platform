package com.touear;

import com.touear.core.cloud.feign.EnableTouearFeign;
import com.touear.core.launch.constant.AppConstant;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableHystrix
@EnableScheduling
@EnableTouearFeign
@SpringCloudApplication
public class TouearManageApplication {


    public static void main(String[] args) {
        TouearApplication.run(AppConstant.APPLICATION_MANAGE_NAME, TouearManageApplication.class, args);
    }

}
