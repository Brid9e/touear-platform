package com.touear.manage.feign;
import com.touear.core.launch.constant.AppConstant;
import com.touear.core.tool.api.R;

import com.touear.manage.entity.BaseAccountEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: Yang
 * @Date: 2024/08/08/12:29
 * @Description: feign-用户信息
 */
@FeignClient(
        value = AppConstant.APPLICATION_MANAGE_NAME,
        fallback = IBaseAccountClientFallback.class
)
public interface BaseAccountClient {

    String API_PREFIX = "/baseaccount";


    /**
     * 根据用户id查询信息
     * @param id
     * @return R<BaseAccountEntity>
     */
    @GetMapping(API_PREFIX + "/getById")
    R<BaseAccountEntity> queryById(@RequestParam Long id);


}
