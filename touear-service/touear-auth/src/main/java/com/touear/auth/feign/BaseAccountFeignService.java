package com.touear.auth.feign;


import com.touear.core.tool.api.R;
import com.touear.manage.entity.BaseAccountEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("touear-manage")
public interface BaseAccountFeignService {
    /**
     * 通过用户名获取用户信息
     * @param account 用户名
     * @return
     */
    @RequestMapping(value = "/manage/baseaccount/getBaseAccountByAccount",method = RequestMethod.GET)
    R getBaseAccountByAccount(@RequestParam String account);
    @GetMapping("/manage/baseaccount/{id}")
    BaseAccountEntity queryById(@PathVariable("id") Long id);
    @GetMapping("/manage/baseaccount/fromUserName/{userName}")
    BaseAccountEntity queryByUserName(@PathVariable String userName);
}