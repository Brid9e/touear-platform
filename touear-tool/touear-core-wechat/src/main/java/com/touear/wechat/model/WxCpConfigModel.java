package com.touear.wechat.model;

import com.touear.wechat.validate.annotation.Validate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Title: WxCpConfigModel
 * @Description: 企业微信应用
 * @author wangqq
 * @date 2023-03-01 16:09:30
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WxCpConfigModel extends WxConfigModel {

    @Validate(groups = WxConfigModel.class, name= "企业id", notNull = true)
    private String corpId;

    @Validate(groups = WxConfigModel.class, name= "应用密钥", notNull = true)
    private String corpSecret;

    @Validate(groups = WxConfigModel.class, name= "应用id", notNull = true)
    private String agentId;

    private String token;

    private String aeskey;

    private String baseApiUrl;

}
