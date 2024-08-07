package com.touear.core.secure.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Title: InterfaceLevelEnum
 * @Description: 接口访问级别
 * @author wangqq
 * @date 2021-11-04 10:09:40
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum InterfaceLevelEnum {

    APP("app", "应用级别"),


    MANAGE("manage", "管理员级别"),
    /**
     * 从app登录
     */
    USER("user", "用户级别");

    /**
     * 类型
     */
    private String code;
    /**
     * 描述
     */
    private String name;

}
