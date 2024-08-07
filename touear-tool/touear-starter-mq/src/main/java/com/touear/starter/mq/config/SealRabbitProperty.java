package com.touear.starter.mq.config;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Title: SealRabbitProperty
 * @Description: RabbitMq消息队列系统参数配置
 * @author wangqq
 * @date 2022-06-15 17:09:33
 * @version 1.0
 */
@Data
public class SealRabbitProperty {

    /**
     * 组件信息
     */
    private List<LinkedHashMap<String, Object>> moduleInfos;

}