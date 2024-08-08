package com.touear.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.touear.manage.entity.BaseAccountEntity;
import com.touear.manage.mapper.BaseAccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.touear.manage.service.BaseAccountService;
import org.springframework.transaction.annotation.Transactional;


@Service("baseAccountService")
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class BaseAccountServiceImpl extends ServiceImpl<BaseAccountMapper, BaseAccountEntity> implements BaseAccountService {

    @Override
    public IPage<BaseAccountEntity> queryPage(Map<String, Object> params) {
//
//        IPage<BaseAccountEntity> page = this.page(
//                new Query<BaseAccountEntity>().getPage(params),
//                new QueryWrapper<BaseAccountEntity>()
//        );
//        return page;
return null;
    }
    /**
     * 通过用户名获取用户密码
     * @param account 用户名
     * @return
     */
    @Override
    public String getPasswordByAccount(String account) {
        BaseAccountEntity baseAccount = baseMapper.selectOne(new LambdaQueryWrapper<BaseAccountEntity>().eq(BaseAccountEntity::getAccount, account));
        return null!=baseAccount?baseAccount.getPassword():"";
    }

    /**
     * 查询用户详情
     *
     * @param id
     */
    @Override
    public BaseAccountEntity queryById(Long id) {
        return this.getById(id);
    }

    /**
     * 查询用户详情
     *
     * @param userName
     */
    @Override
    public BaseAccountEntity queryByUserName(String userName) {
        return this.getOne(new QueryWrapper<BaseAccountEntity>().eq("account", userName));
    }


}