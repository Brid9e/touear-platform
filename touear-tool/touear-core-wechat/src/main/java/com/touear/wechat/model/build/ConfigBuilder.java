package com.touear.wechat.model.build;

import com.touear.core.tool.exception.ServiceException;
import com.touear.wechat.model.WxConfigModel;

import java.util.Map;

/**
 * @Title: Builder
 * @Description: 构建微信参数
 * @author wangqq
 * @date 2023-06-29 15:36:56
 * @version 1.0
 */
public interface ConfigBuilder {

    WxConfigModel build(Map<String, String> configMap) throws ServiceException;

    WxConfigModel build(Map<String, String> configMap, Class<? extends WxConfigModel> clazz) throws ServiceException;

}
