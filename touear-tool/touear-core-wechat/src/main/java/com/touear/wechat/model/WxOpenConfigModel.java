package com.touear.wechat.model;

import com.touear.wechat.validate.annotation.Validate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Title: WxOpenConfigModel
 * @Description: 微信开放平台
 * @author wangqq
 * @date 2023-03-02 14:15:00
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WxOpenConfigModel extends WxConfigModel {

    @Validate(groups = WxConfigModel.class, name= "appid", notNull = true)
    private String appId;

    @Validate(groups = WxConfigModel.class, name= "密钥", notNull = true)
    private String secret;


    private String token;

}
