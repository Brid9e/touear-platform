package com.touear.starter.mq.config;

import com.touear.starter.mq.contant.ExchangeTypeConstant;
import lombok.Data;

import java.util.Map;

/**
 * @Title: ExchangeTypeEnum
 * @Description: RabbitMq的队列，交换机，绑定关系的对象
 * @author wangqq
 * @date 2022-06-15 17:09:33
 * @version 1.0
 */
@Data
public class RabbitModuleInfo {

    /**
     * 路由主键
     */
    private String routingKey;

    /**
     * 队列信息
     */
    private Queue queue;

    /**
     * 交换机信息
     */
    private Exchange exchange;

    /**
     * 交换机的详细配置
     */
    @Data
    public static class Exchange {

        /**
         * 交换机名称
         */
        private String name;

        /**
         * 交换机类型。
         * 默认：直连型号
         */
        private String type = ExchangeTypeConstant.DIRECT;

        /**
         * 是否持久化
         * 默认true：当RabbitMq重启时，消息不丢失
         */
        private boolean durable = true;

        /**
         * 当所有绑定队列都不在使用时，是否自动 删除交换器
         * 默认值false：不自动删除，推荐使用。
         */
        private boolean autoDelete = false;

        /**
         * 判断是否是延迟交换机
         */
        private boolean delayed;

        /**
         * 交换机的额外参数
         */
        private Map<String, Object> arguments;

    }


    /**
     * 队列的详细信息
     * 提供默认的配置参数
     */
    @Data
    public static class Queue {

        /**
         * 队列名
         * 必填
         */
        private String name;

        /**
         * 是否持久化
         * 默认true：当RabbitMq重启时，消息不丢失
         */
        private boolean durable = true;

        /**
         * 是否具有排他性
         * 默认false：可以多个消息者消费同一个队列
         */
        private boolean exclusive = false;

        /**
         * 当消费者客户端均断开连接，是否自动删除队列
         * 默认值false：不自动删除，推荐使用，避免消费者断开后，队列中丢弃消息
         */
        private boolean autoDelete = false;

        /**
         * 需要绑定的死信队列的交换机名称
         */
        private String deadExchangeName;

        /**
         * 需要绑定的死信队列的路由key的名称
         */
        private String deadRoutingKey;

        /**
         * 队列的额外参数
         */
        private Map<String, Object> arguments;

    }


}