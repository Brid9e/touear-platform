package com.touear.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.touear.manage.entity.BaseAccountEntity;

import java.util.Map;

/**
 * 用户信息表
 *
 * @author sjw
 * @email ${email}
 * @date 2024-08-05 23:15:20
 */
public interface BaseAccountService extends IService<BaseAccountEntity> {

    IPage<BaseAccountEntity> queryPage(Map<String, Object> params);


    /**
     * 通过用户名获取用户密码
     * @param account 用户名
     * @return
     */

    String getPasswordByAccount(String account);

    BaseAccountEntity queryById(Long id);
    /**查询用户详情*/
    BaseAccountEntity queryByUserName(String userName);
}





