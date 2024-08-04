package com.touear.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touear.manage.entity.BaseLabelsEntity;
import com.touear.manage.mapper.BaseLabelsMapper;
import com.touear.manage.service.BaseLabelsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BaseLabelsServiceImpl extends ServiceImpl<BaseLabelsMapper, BaseLabelsEntity> implements BaseLabelsService {
}
