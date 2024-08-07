package com.touear.manage.controller;

import java.util.Arrays;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.touear.core.tool.api.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.touear.manage.entity.BaseAccountEntity;
import com.touear.manage.service.BaseAccountService;




/**
 * 用户信息表
 *
 * @author sjw
 * @email ${email}
 * @date 2024-08-05 23:15:20
 */
@Slf4j
@RestController
@RequestMapping("manage/baseaccount")
public class BaseAccountController {
    @Autowired
    private BaseAccountService baseAccountService;
    @GetMapping(value = "/{id}")
    public BaseAccountEntity queryById(@PathVariable("id") Long id) {
        return baseAccountService.queryById(id);
    }

    @GetMapping(value = "fromUserName/{userName}")
    public BaseAccountEntity loadUserByUsername(@PathVariable("userName") String userName) {

        return baseAccountService.queryByUserName(userName);
    }
    /**
     * 通过用户名获取用户密码
     * @param account 用户名
     * @return
     */
    @RequestMapping(value = "/getBaseAccountByAccount",method = RequestMethod.GET)
    public R getPasswordByAccount(String account){
        String password = baseAccountService.getPasswordByAccount(account);

        return R.data(password);
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public R<IPage<BaseAccountEntity>> list(@RequestParam Map<String, Object> params){
        IPage<BaseAccountEntity> page = baseAccountService.queryPage(params);

        return R.data(page);
    }


    /**
     * 信息
     */
    @RequestMapping(value = "/info/{id}",method = RequestMethod.GET)
    public R info(@PathVariable("id") Integer id){
        BaseAccountEntity baseAccount = baseAccountService.getById(id);

        return R.data( baseAccount);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public R save(@RequestBody BaseAccountEntity baseAccount){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = baseAccount.getPassword();
        String encode = passwordEncoder.encode(password);
        baseAccount.setPassword(encode);
        baseAccountService.save(baseAccount);
        return R.success("新增成功");
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public R update(@RequestBody BaseAccountEntity baseAccount){
        baseAccountService.updateById(baseAccount);

        return R.success("修改成功");
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public R delete(@RequestBody Integer[] ids){
        baseAccountService.removeByIds(Arrays.asList(ids));

        return R.success("删除成功");
    }

}
