package com.touear.client.controller;

import com.touear.core.tool.api.R;
import com.touear.manage.entity.BaseAccountEntity;
import com.touear.manage.feign.BaseAccountClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: Yang
 * @Date: 2024/08/08/12:56
 * @Description:
 */

@RestController
@RequestMapping("client/test")
public class TestController {

    @Autowired
    private BaseAccountClient accountClient;



    @GetMapping(value = "/user")
    public R<Boolean> getUser() {
        R<BaseAccountEntity> accountR = accountClient.queryById(1L);
        if(accountR.isSuccess()){
            return R.data(true);
        }
        return R.data(false) ;
    }
}
