package com.touear.wechat;

import com.touear.wechat.model.build.ConfigBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: WxHolder
 * @Description: 微信配置
 * @author wangqq
 * @date 2023-02-24 15:36:56
 * @version 1.0
 */
@Service
public class WxHolder {

    private final Map<String, ConfigBuilder> builderHolder = new HashMap<>();

    /**
     * 根据key，获取ConfigBuilder对象，去进行配置初始化
     *
     * @param key
     * @return {@link ConfigBuilder}
     * @author wangqq
     * @date 2023-10-12 09:22:15
     */
    public ConfigBuilder getWxConfigBuilder(String key) {
        return builderHolder.get(key);
    }

    public void putConfigBuilder(String key, ConfigBuilder builder) {
        builderHolder.put(key, builder);
    }

}
