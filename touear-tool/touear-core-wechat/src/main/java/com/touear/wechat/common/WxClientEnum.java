package com.touear.wechat.common;

import com.touear.core.tool.utils.Func;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @Title: WechatEnum
 * @Description: 微信客户端类型
 * @author wangqq
 * @date 2023-02-24 15:36:56
 * @version 1.0
 */
@Getter
@ToString
@AllArgsConstructor
public enum WxClientEnum {

    MP("wechat-mp", "微信公众号"),
    MINA("wechat-mina", "微信小程序"),
    WORK("wechat-work", "企业微信"),
    OPEN("wechat-open", "微信开放平台");

    private final String code;
    private final String name;

    public static String getNameByCode(String code) {
        WxClientEnum[] values = WxClientEnum.values();
        for (WxClientEnum wechatEnum : values) {
            if (Func.equals(wechatEnum.code,code)) {
                return wechatEnum.getName();
            }
        }
        return null;
    }

}
