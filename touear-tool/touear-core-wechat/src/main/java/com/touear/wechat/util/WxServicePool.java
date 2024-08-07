package com.touear.wechat.util;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedisBetterConfigImpl;
import com.google.common.base.Throwables;
import com.touear.core.tool.utils.StringPool;
import com.touear.wechat.common.WxClientEnum;
import com.touear.wechat.model.*;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.impl.WxCpRedissonConfigImpl;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.impl.WxOpenInRedisTemplateConfigStorage;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Title: WxServicePool
 * @Description: 获取微信对象实例工具类
 * @author wangqq
 * @date 2022-12-29 09:17:42
 * @version 1.0
 */
@Slf4j
@Configuration
public class WxServicePool {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /** 微信服务号 */
    private final Map<String, WxMpService> wxMpServiceMap = new ConcurrentHashMap<>();
    /** 微信小程序 */
    private final Map<String, WxMaService> wxMaServiceMap = new ConcurrentHashMap<>();
    /** 企业微信（微信企业号） */
    private final Map<String, WxCpService> wxCpServiceMap = new ConcurrentHashMap<>();
    /** 微信开放平台 */
    private final Map<String, WxOpenService> wxOpenServiceMap = new ConcurrentHashMap<>();

    @Bean
    public RedisTemplateWxRedisOps redisTemplateWxRedisOps() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate(redisConnectionFactory);
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        return new RedisTemplateWxRedisOps(redisTemplate);
    }

    /**
     * 根据配置，获取并初始化微信相关的service接口
     *
     * @param channelModel
     * @param clazz
     * @return {@link T}
     * @author wangqq
     * @date 2023-10-12 09:23:36
     */
    @SuppressWarnings("unchecked")
    public <T> T getWechatService(WxConfigModel channelModel, Class<T> clazz) {
        try {
            if (clazz.equals(WxMpService.class)) {
                WxMpConfigModel wxMpConfigModel = (WxMpConfigModel) channelModel;
                return (T) ConcurrentHashMapUtils.computeIfAbsent(wxMpServiceMap, wxMpConfigModel.getAppId(), appId -> initWxMpService(wxMpConfigModel));
            } else if (clazz.equals(WxMaService.class)) {
                WxMaConfigModel wxMaConfigModel = (WxMaConfigModel) channelModel;
                return (T) ConcurrentHashMapUtils.computeIfAbsent(wxMaServiceMap, wxMaConfigModel.getAppId(), appId -> initWxMaService(wxMaConfigModel));
            } else if (clazz.equals(WxCpService.class)) {
                WxCpConfigModel wxCpConfigModel = (WxCpConfigModel) channelModel;
                String key = wxCpConfigModel.getCorpId() + StringPool.DASH + wxCpConfigModel.getAgentId();
                return (T) ConcurrentHashMapUtils.computeIfAbsent(wxCpServiceMap, key, appId -> initWxCpService(wxCpConfigModel));
            } else if (clazz.equals(WxOpenService.class)) {
                WxOpenConfigModel wxOpenConfigModel = (WxOpenConfigModel) channelModel;
                return (T) ConcurrentHashMapUtils.computeIfAbsent(wxOpenServiceMap, wxOpenConfigModel.getAppId(), appId -> initWxOpenService(wxOpenConfigModel));
            }
        } catch (Exception e) {
            log.error("WeChatUtil， getService fail! e:{}", Throwables.getStackTraceAsString(e));
        }
        return null;
    }

    /**
     * 初始化微信服务号，并将access_token 用redis存储
     *
     * @param wxMpConfigModel  微信服务号的配置
     * @return {@link WxMpService}
     * @author wangqq
     * @date 2023-03-03 08:23:52
     */
    private WxMpService initWxMpService(WxMpConfigModel wxMpConfigModel) {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpRedisConfigImpl config = new WxMpRedisConfigImpl(redisTemplateWxRedisOps(), WxClientEnum.MP.getCode());
        config.setAppId(wxMpConfigModel.getAppId());
        config.setSecret(wxMpConfigModel.getSecret());
        config.setAccessToken(wxMpConfigModel.getToken());
        wxMpService.setWxMpConfigStorage(config);
        return wxMpService;
    }


    /**
     * 初始化微信小程序，并将access_token 用redis存储
     *
     * @param wxMaConfigModel      微信小程序的通道配置
     * @return {@link WxMaService}
     * @author wangqq
     * @date 2023-03-03 08:25:09
     */
    private WxMaService initWxMaService(WxMaConfigModel wxMaConfigModel) {
        WxMaService wxMaService = new WxMaServiceImpl();
        WxMaRedisBetterConfigImpl config = new WxMaRedisBetterConfigImpl(redisTemplateWxRedisOps(), WxClientEnum.MINA.getCode());
        config.setAppid(wxMaConfigModel.getAppId());
        config.setSecret(wxMaConfigModel.getSecret());
        config.setAccessToken(wxMaConfigModel.getToken());
        wxMaService.setWxMaConfig(config);
        return wxMaService;
    }

    /**
     * 初始化企业微信
     *
     * @param wxCpConfigModel      企业微信的通道配置
     * @return {@link WxCpService}
     * @author wangqq
     * @date 2023-03-03 08:25:09
     */
    private WxCpService initWxCpService(WxCpConfigModel wxCpConfigModel) {
        WxCpService wxCpService = new WxCpServiceImpl();
        WxCpRedissonConfigImpl config = new WxCpRedissonConfigImpl(redisTemplateWxRedisOps(), WxClientEnum.WORK.getCode());
        config.setCorpId(wxCpConfigModel.getCorpId());
        config.setCorpSecret(wxCpConfigModel.getCorpSecret());
        config.setAgentId(Integer.parseInt(wxCpConfigModel.getAgentId()));
        config.setToken(wxCpConfigModel.getToken());
        config.setAesKey(wxCpConfigModel.getAeskey());
        config.setBaseApiUrl(wxCpConfigModel.getBaseApiUrl());
        wxCpService.setWxCpConfigStorage(config);
        return wxCpService;
    }


    /**
     * 初始化微信开放平台
     *
     * @param wxOpenConfigModel      微信开放平台的通道配置
     * @return {@link WxOpenService}
     * @author wangqq
     * @date 2023-03-03 08:25:09
     */
    private WxOpenService initWxOpenService(WxOpenConfigModel wxOpenConfigModel) {
        WxOpenService wxOpenService = new WxOpenServiceImpl();
        WxOpenInRedisTemplateConfigStorage config = new WxOpenInRedisTemplateConfigStorage(redisTemplateWxRedisOps(), WxClientEnum.OPEN.getCode());
        config.setComponentAppId(wxOpenConfigModel.getAppId());
        config.setComponentAppSecret(wxOpenConfigModel.getSecret());
        wxOpenService.setWxOpenConfigStorage(config);
        return wxOpenService;
    }

}
