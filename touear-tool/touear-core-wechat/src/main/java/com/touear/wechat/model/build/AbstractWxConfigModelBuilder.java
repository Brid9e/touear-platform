package com.touear.wechat.model.build;

import com.touear.core.tool.jackson.JsonUtil;
import com.touear.core.tool.utils.Func;
import com.touear.core.tool.utils.ValidatorUtils;
import com.touear.wechat.WxHolder;
import com.touear.wechat.model.WxConfigModel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @Title: AbstractWechatModelBuilder
 * @Description: 微信参数封装类
 * @author wangqq
 * @date 2023-06-29 13:35:27
 * @version 1.0
 */
public abstract class AbstractWxConfigModelBuilder implements ConfigBuilder {

    protected String wxCode;

    @Autowired
    private WxHolder wxHolder;

    @PostConstruct
    public void init() {
        wxHolder.putConfigBuilder(wxCode, this);
    }


    public WxConfigModel getWechatModelByConfig(Map<String, String> configMap, Class<? extends WxConfigModel> clazz) {
        ValidatorUtils.checkService(Func.isNotEmpty(configMap), "微信配置有误");
        return JsonUtil.toPojo(configMap, clazz);
    }

}
