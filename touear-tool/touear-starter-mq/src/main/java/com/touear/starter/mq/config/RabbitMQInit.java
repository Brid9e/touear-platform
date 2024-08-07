package com.touear.starter.mq.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Title: RabbitMQInit.java
 * @Description: 初始化RabbitMQ
 * @author wangqq
 * @date 2020年6月22日 上午8:55:19
 * @version 1.0
 */
@Data
@Configuration
@AllArgsConstructor
@ConfigurationProperties(prefix = "touear")
public class RabbitMQInit {

    private final AmqpAdmin amqpAdmin;

    private List<RabbitModuleInfo> rabbitmq;

    @Bean
    @ConditionalOnMissingBean
    public DeclareRabbitModule declareRabbitModule() {
        return new DeclareRabbitModule(amqpAdmin, rabbitmq);
    }

}

