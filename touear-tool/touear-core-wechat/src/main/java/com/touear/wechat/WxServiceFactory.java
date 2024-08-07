package com.touear.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.touear.wechat.common.WxClientEnum;
import com.touear.wechat.model.*;
import com.touear.wechat.util.WxServicePool;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.open.api.WxOpenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Title: WxServiceFactory
 * @Description: 微信工厂类
 * @author wangqq
 * @date 2023-06-29 09:39:29
 * @version 1.0
 */
@Slf4j
@Service
public class WxServiceFactory {

    @Autowired
    private WxServicePool wxServicePool;
    @Autowired
    private WxHolder wxHolder;

    /**
     * 根据公众号的配置参数，获取微信公众号的业务实现接口WxMpService，可能为空，需判断非空情况
     *
     * @param wxConfigModel
     * @return {@link WxMpService}
     * @author wangqq
     * @date 2023-10-12 08:54:51
     */
    public WxMpService getWxMpService(WxMpConfigModel wxConfigModel) {
        if (wxConfigModel != null) {
            return wxServicePool.getWechatService(wxConfigModel, WxMpService.class);
        }
        return null;
    }

    /**
     * 根据小程序的配置参数，获取小程序的业务实现接口WxMaService，可能为空，需判断非空情况
     *
     * @param wxConfigModel
     * @return {@link WxMaService}
     * @author wangqq
     * @date 2023-10-12 08:55:58
     */
    public WxMaService getWxMaService(WxMaConfigModel wxConfigModel) {
        if (wxConfigModel != null) {
            return wxServicePool.getWechatService(wxConfigModel, WxMaService.class);
        }
        return null;
    }

    /**
     * 根据企业微信的配置参数，获取企业微信的业务实现接口WxCpService，可能为空，需判断非空情况
     *
     * @param wxConfigModel
     * @return {@link WxCpService}
     * @author wangqq
     * @date 2023-10-12 08:57:38
     */
    public WxCpService getWxCpService(WxCpConfigModel wxConfigModel) {
        if (wxConfigModel != null) {
            return wxServicePool.getWechatService(wxConfigModel, WxCpService.class);
        }
        return null;
    }

    /**
     * 根据微信开放平台的配置参数，获取微信开放平台的业务实现接口WxOpenService，可能为空，需判断非空情况
     *
     * @param wxConfigModel
     * @return {@link WxCpService}
     * @author wangqq
     * @date 2023-10-12 08:57:38
     */
    public WxOpenService getWxOpenService(WxOpenConfigModel wxConfigModel) {
        if (wxConfigModel != null) {
            return wxServicePool.getWechatService(wxConfigModel, WxOpenService.class);
        }
        return null;
    }

    /**
     * 根据公众号的配置参数，获取微信公众号的业务实现接口WxMpService，可能为空，需判断非空情况
     *
     * @param wechatConfig
     * @return {@link WxMpService}
     * @author wangqq
     * @date 2023-10-12 08:59:03
     */
    public WxMpService getWxMpService(Map<String, String> wechatConfig) {
        WxConfigModel wxConfigModel = getWxMpConfigModel(wechatConfig);
        if (wxConfigModel != null) {
            return wxServicePool.getWechatService(wxConfigModel, WxMpService.class);
        }
        return null;
    }

    /**
     * 根据小程序的配置参数，获取小程序的业务实现接口WxMaService，可能为空，需判断非空情况
     *
     * @param wechatConfig
     * @return {@link WxMaService}
     * @author wangqq
     * @date 2023-10-12 09:12:06
     */
    public WxMaService getWxMaService(Map<String, String> wechatConfig) {
        WxConfigModel wxConfigModel = getWxMaConfigModel(wechatConfig);
        if (wxConfigModel != null) {
            return wxServicePool.getWechatService(wxConfigModel, WxMaService.class);
        }
        return null;
    }

    /**
     * 根据企业微信的配置参数，获取企业微信的业务实现接口WxCpService，可能为空，需判断非空情况
     *
     * @param wechatConfig
     * @return {@link WxCpService}
     * @author wangqq
     * @date 2023-10-12 09:12:55
     */
    public WxCpService getWxCpService(Map<String, String> wechatConfig) {
        WxConfigModel wxConfigModel = getWxCpConfigModel(wechatConfig);
        if (wxConfigModel != null) {
            return wxServicePool.getWechatService(wxConfigModel, WxCpService.class);
        }
        return null;
    }

    /**
     * 根据微信开放平台的配置参数，获取微信开放平台的业务实现接口WxOpenService，可能为空，需判断非空情况
     *
     * @param wechatConfig
     * @return {@link WxOpenService}
     * @author wangqq
     * @date 2023-10-12 09:13:16
     */
    public WxOpenService getWxOpenService(Map<String, String> wechatConfig) {
        WxConfigModel wxConfigModel = getWxOpenConfigModel(wechatConfig);
        if (wxConfigModel != null) {
            return wxServicePool.getWechatService(wxConfigModel, WxOpenService.class);
        }
        return null;
    }

    /**
     * 将公众号的配置参数，转为配置对象
     *
     * @param wechatConfig
     * @return {@link WxConfigModel}
     * @author wangqq
     * @date 2023-10-12 09:13:39
     */
    public WxMpConfigModel getWxMpConfigModel(Map<String, String> wechatConfig) {
        return (WxMpConfigModel) wxHolder.getWxConfigBuilder(WxClientEnum.MP.getCode()).build(wechatConfig);
    }

    /**
     * 将公众号的配置参数，转为配置对象
     *
     * @param wechatConfig
     * @return {@link WxConfigModel}
     * @author wangqq
     * @date 2023-10-12 09:13:39
     */
    public <T extends WxMpConfigModel> T getWxMpConfigModel(Map<String, String> wechatConfig, Class<T> clazz) {
        return (T) wxHolder.getWxConfigBuilder(WxClientEnum.MP.getCode()).build(wechatConfig, clazz);
    }

    /**
     * 将小程序的配置参数，转为配置对象
     *
     * @param wechatConfig
     * @return {@link WxConfigModel}
     * @author wangqq
     * @date 2023-10-12 09:14:49
     */
    public <T extends WxMaConfigModel> T getWxMaConfigModel(Map<String, String> wechatConfig, Class<T> clazz) {
        return (T) wxHolder.getWxConfigBuilder(WxClientEnum.MINA.getCode()).build(wechatConfig, clazz);
    }

    /**
     * 将小程序的配置参数，转为配置对象
     *
     * @param wechatConfig
     * @return {@link WxConfigModel}
     * @author wangqq
     * @date 2023-10-12 09:14:49
     */
    public WxMaConfigModel getWxMaConfigModel(Map<String, String> wechatConfig) {
        return (WxMaConfigModel) wxHolder.getWxConfigBuilder(WxClientEnum.MINA.getCode()).build(wechatConfig);
    }

    /**
     * 将企业微信的配置参数，转为配置对象
     *
     * @param wechatConfig
     * @return {@link WxConfigModel}
     * @author wangqq
     * @date 2023-10-12 09:15:08
     */
    public <T extends WxCpConfigModel> T getWxCpConfigModel(Map<String, String> wechatConfig, Class<T> clazz) {
        return (T) wxHolder.getWxConfigBuilder(WxClientEnum.WORK.getCode()).build(wechatConfig, clazz);
    }

    /**
     * 将企业微信的配置参数，转为配置对象
     *
     * @param wechatConfig
     * @return {@link WxConfigModel}
     * @author wangqq
     * @date 2023-10-12 09:15:08
     */
    public WxCpConfigModel getWxCpConfigModel(Map<String, String> wechatConfig) {
        return (WxCpConfigModel) wxHolder.getWxConfigBuilder(WxClientEnum.WORK.getCode()).build(wechatConfig);
    }

    /**
     * 将微信开放平台的配置参数，转为配置对象
     *
     * @param wechatConfig
     * @return {@link WxConfigModel}
     * @author wangqq
     * @date 2023-10-12 09:15:27
     */
    public <T extends WxOpenConfigModel> T getWxOpenConfigModel(Map<String, String> wechatConfig, Class<T> clazz) {
        return (T) wxHolder.getWxConfigBuilder(WxClientEnum.OPEN.getCode()).build(wechatConfig, clazz);
    }

    /**
     * 将微信开放平台的配置参数，转为配置对象
     *
     * @param wechatConfig
     * @return {@link WxConfigModel}
     * @author wangqq
     * @date 2023-10-12 09:15:27
     */
    public WxOpenConfigModel getWxOpenConfigModel(Map<String, String> wechatConfig) {
        return (WxOpenConfigModel) wxHolder.getWxConfigBuilder(WxClientEnum.OPEN.getCode()).build(wechatConfig);
    }

}
