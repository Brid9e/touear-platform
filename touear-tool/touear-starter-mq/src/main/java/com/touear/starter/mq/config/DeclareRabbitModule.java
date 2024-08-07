package com.touear.starter.mq.config;

import com.touear.starter.mq.contant.ExchangeTypeConstant;
import com.touear.starter.mq.exception.RabbitDeclareModuleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: DeclareRabbitModule
 * @Description: RabbitMq的队列，交换机，绑定关系的对象
 * @author wangqq
 * @date 2022-06-15 17:09:33
 * @version 1.0
 */
@Slf4j
public class DeclareRabbitModule implements SmartInitializingSingleton {

    /**
     * 死信队列交换机标识符
     */
    public static final String DEAD_LETTER_QUEUE_KEY = "x-dead-letter-exchange";

    /**
     * 死信队列交换机绑定键标识符
     */
    public static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

    private final AmqpAdmin amqpAdmin;

    private final List<RabbitModuleInfo> moduleInfos;

    public DeclareRabbitModule(AmqpAdmin amqpAdmin, List<RabbitModuleInfo> moduleInfos) {
        this.amqpAdmin = amqpAdmin;
        this.moduleInfos = moduleInfos;
    }

    @Override
    public void afterSingletonsInstantiated() {
        log.info("Dynamically create RabbitMQ information...");
        // 获取组件信息
        if (CollectionUtils.isEmpty(moduleInfos)) {
            log.warn("The configuration does not contain RabbitMQ information, Do not register!");
            return;
        }
        // 注册RabbitMQ信息
        StringBuilder queueNames = new StringBuilder();
        StringBuilder exchangeNames = new StringBuilder();
        for (RabbitModuleInfo moduleInfo : moduleInfos) {
            if (declareModule(moduleInfo)) {
                if (moduleInfo.getQueue() != null)
                    queueNames.append(", ").append(moduleInfo.getQueue().getName());
                if (moduleInfo.getExchange() != null)
                    exchangeNames.append(", ").append(moduleInfo.getExchange().getName());
            }
        }
        if (queueNames.length() != 0) {
            log.info("create queue is: {}", queueNames.substring(2));
        }
        if (exchangeNames.length() != 0) {
            log.info("create exchange is: {}" , exchangeNames.substring(2));
        }
    }


    /**
     * 动态创建MQ配置信息
     *
     * @author wangqq
     * @date 2022-06-15 17:25:51
     */
    public boolean declareModule(RabbitModuleInfo moduleInfo) {
        try {
            // 数据校验
            declareValidate(moduleInfo);
            // 获取队列
            Queue queue = tranSealQueue(moduleInfo.getQueue());
            if (queue != null) {
                // 创建队列
                amqpAdmin.declareQueue(queue);
            }
            // 获取交换机
            Exchange exchange = tranSealExchange(moduleInfo.getExchange());
            if (exchange != null) {
                amqpAdmin.declareExchange(exchange);
                if (queue != null) {
                    // 绑定关系
                    Binding binding = new Binding(queue.getName(), Binding.DestinationType.QUEUE,
                            exchange.getName(), moduleInfo.getRoutingKey(), moduleInfo.getExchange().getArguments());
                    amqpAdmin.declareBinding(binding);
                }
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }


    /**
     * 声明模块数据校验
     *
     * @param moduleInfo  配置文件的模块信息
     * @author wangqq
     * @date 2022-06-15 17:24:03
     */
    private void declareValidate(RabbitModuleInfo moduleInfo) throws RabbitDeclareModuleException {
        // 判断关键参数是否存在且合法
        if (moduleInfo.getQueue() == null && moduleInfo.getExchange() == null) {
            throw new RabbitDeclareModuleException(String.format("The RabbitMQ configuration is incorrect, queue and exchange are null, the routingKey is: [%s]!", moduleInfo.getRoutingKey()));
        }
        if (moduleInfo.getQueue() != null) {
            if (moduleInfo.getQueue().getName() == null) {
                throw new RabbitDeclareModuleException(String.format("The RabbitMQ configuration is incorrect, queue's name is null, the routingKey is: [%s]!", moduleInfo.getRoutingKey()));
            }
        }
        if (moduleInfo.getExchange() != null) {
            if (moduleInfo.getQueue() != null && moduleInfo.getRoutingKey() == null) {
                throw new RabbitDeclareModuleException(String.format("The RabbitMQ configuration is incorrect, RoutingKey is null, the queue's name is: [%s]!", moduleInfo.getQueue().getName()));
            }
            if (moduleInfo.getExchange().getName() == null) {
                throw new RabbitDeclareModuleException(String.format("The RabbitMQ configuration is incorrect, exchange's name is null, the routingKey is: [%s]!", moduleInfo.getRoutingKey()));
            }
        }
    }


    /**
     * 队列的对象转换
     *
     * @param queue     自定义的队列信息
     * @return {@link Queue}    RabbitMq的Queue对象
     * @author wangqq
     * @date 2022-06-15 17:23:23
     */
    private Queue tranSealQueue(RabbitModuleInfo.Queue queue) {
        if (queue != null) {
            Map<String, Object> arguments = queue.getArguments();
            // 判断是否需要绑定死信队列
            if (queue.getDeadExchangeName() != null && queue.getDeadRoutingKey() != null) {
                // 设置响应参数
                if (queue.getArguments() == null) {
                    arguments = new HashMap<>(2);
                }
                arguments.put(DEAD_LETTER_QUEUE_KEY, queue.getDeadExchangeName());
                arguments.put(DEAD_LETTER_ROUTING_KEY, queue.getDeadRoutingKey());
            }
            return new Queue(queue.getName(), queue.isDurable(), queue.isExclusive(), queue.isAutoDelete(), arguments);
        }
        return null;
    }


    /**
     * 将配置信息转换交换机信息
     *
     * @param exchangeInfo  自定义交换机信息
     * @return {@link Exchange} RabbitMq的Exchange的信息
     * @author wangqq
     * @date 2022-06-15 17:22:29
     */
    private Exchange tranSealExchange(RabbitModuleInfo.Exchange exchangeInfo) {
        AbstractExchange exchange = null;
        if (exchangeInfo != null) {
            // 判断类型
            switch (exchangeInfo.getType()) {
                // 直连模式
                case ExchangeTypeConstant.DIRECT:
                    exchange = new DirectExchange(exchangeInfo.getName(), exchangeInfo.isDurable(), exchangeInfo.isAutoDelete(), exchangeInfo.getArguments());
                    break;
                // 广播模式：
                case ExchangeTypeConstant.FANOUT:
                    exchange = new FanoutExchange(exchangeInfo.getName(), exchangeInfo.isDurable(), exchangeInfo.isAutoDelete(), exchangeInfo.getArguments());
                    break;
                // 通配符模式
                case ExchangeTypeConstant.TOPIC:
                    exchange = new TopicExchange(exchangeInfo.getName(), exchangeInfo.isDurable(), exchangeInfo.isAutoDelete(), exchangeInfo.getArguments());
                    break;
                // headers模式
                case ExchangeTypeConstant.HEADERS:
                    exchange = new HeadersExchange(exchangeInfo.getName(), exchangeInfo.isDurable(), exchangeInfo.isAutoDelete(), exchangeInfo.getArguments());
                    break;
            }
            // 设置延迟队列
            if (exchange != null) {
                exchange.setDelayed(exchangeInfo.isDelayed());
            }
        }
        return exchange;
    }

}