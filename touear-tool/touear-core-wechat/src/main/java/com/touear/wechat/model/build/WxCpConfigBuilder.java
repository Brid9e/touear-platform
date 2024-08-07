package com.touear.wechat.model.build;

import com.touear.core.tool.exception.ServiceException;
import com.touear.wechat.common.WxClientEnum;
import com.touear.wechat.model.WxConfigModel;
import com.touear.wechat.model.WxCpConfigModel;
import com.touear.wechat.validate.util.ValidateUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Title: WxCpConfigBuilder
 * @Description: 企业微信（微信企业号）
 * @author wangqq
 * @date 2023-06-29 13:40:12
 * @version 1.0
 */
@Service
public class WxCpConfigBuilder extends AbstractWxConfigModelBuilder implements ConfigBuilder {

    public WxCpConfigBuilder() {
        wxCode = WxClientEnum.WORK.getCode();
    }

    @Override
    public WxConfigModel build(Map<String, String> configMap) {
        return build(configMap, WxCpConfigModel.class);
    }

    @Override
    public WxConfigModel build(Map<String, String> configMap, Class<? extends WxConfigModel> clazz) throws ServiceException {
        configMap.put("corpId", configMap.get("corpid"));
        configMap.put("corpSecret", configMap.get("corpsecret"));
        configMap.put("agentId", configMap.get("agentid"));
        WxConfigModel wechatModel = getWechatModelByConfig(configMap, clazz);
        ValidateUtil.check(wechatModel, clazz);
        return wechatModel;
    }
}
