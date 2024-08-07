package com.touear.auth.serivce;

import com.alibaba.fastjson.JSON;
import com.touear.auth.feign.BaseAccountFeignService;
import com.touear.manage.entity.BaseAccountEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class UserServiceImpl implements UserDetailsService {

    /**
     * 查询用户服务的 Feign
     */
    @Resource
    private BaseAccountFeignService baseAccountFeignService;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        //根据用户ID来查询用户信息
        BaseAccountEntity userInfo = baseAccountFeignService.queryByUserName(s);

        if (userInfo==null) {
            return null;
        }

        //TODO 定义权限
        String[] authorities = {"admin"};
        //用户的密码
        String password = userInfo.getPassword();
        //扩展存储用户信息
        Map<String,Object> map = new HashMap<>();
        map.put("userName",s);
        map.put("userId",userInfo.getId());
        //转JSON
        String jsonString = JSON.toJSONString(map);
        //构建
        UserDetails userDetails =
                User.withUsername(jsonString)
                .password(password).authorities(authorities).build();
        return userDetails;
    }
}
