package com.touear.wechat.model.build;

import com.touear.core.tool.exception.ServiceException;
import com.touear.wechat.common.WxClientEnum;
import com.touear.wechat.model.WxConfigModel;
import com.touear.wechat.model.WxMpConfigModel;
import com.touear.wechat.validate.util.ValidateUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Title: WxOpenConfigBuilder
 * @Description: 微信开放平台
 * @author wangqq
 * @date 2023-06-29 13:40:12
 * @version 1.0
 */
@Service
public class WxOpenConfigBuilder extends AbstractWxConfigModelBuilder implements ConfigBuilder {

    public WxOpenConfigBuilder() {
        wxCode = WxClientEnum.OPEN.getCode();
    }

    @Override
    public WxConfigModel build(Map<String, String> configMap) {
        return build(configMap, WxMpConfigModel.class);
    }

    @Override
    public WxConfigModel build(Map<String, String> configMap, Class<? extends WxConfigModel> clazz) throws ServiceException {
        WxConfigModel wechatModel = getWechatModelByConfig(configMap, clazz);
        ValidateUtil.check(wechatModel, clazz);
        return wechatModel;
    }

}
