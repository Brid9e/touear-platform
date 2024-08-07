package com.touear.manage.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touear.manage.entity.BaseSysconfig;
import com.touear.manage.mappers.BaseSysconfigMapper;
import com.touear.manage.service.BaseSysconfigService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Title: BaseSysconfigServiceImpl.java
 * @Description: BaseSysconfigServiceImpl
 * @author chenl
 * @date 2020-08-05 10:03:53
 * @version 1.0
 */

@Service
@AllArgsConstructor
@Log4j2
public class BaseSysconfigServiceImpl extends ServiceImpl<BaseSysconfigMapper, BaseSysconfig>
		implements BaseSysconfigService {

	private RedisTemplate<String, String> redisTemplate;





	@Override
	public List<BaseSysconfig> selectList(BaseSysconfig systemConfig) {
		return this.list(new QueryWrapper<>(systemConfig));
	}

	@Override

	public BaseSysconfig selectSysconfig(String code) {
		QueryWrapper<BaseSysconfig> wrapper = new QueryWrapper();
		wrapper.lambda().eq(BaseSysconfig::getCode,code);
		return this.getOne(wrapper);
	}




}
