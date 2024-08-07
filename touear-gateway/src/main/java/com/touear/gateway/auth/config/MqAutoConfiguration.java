package com.touear.gateway.auth.config;

import com.touear.common.constant.RabbitConstant.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Slf4j
@Configuration
public class MqAutoConfiguration {
	@Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue accessLogsQueue() {
        Queue queue = new Queue(Log.LOG_ACCESS_QUEUE);
        log.info("Query {} [{}]", Log.LOG_ACCESS_QUEUE, queue);
        return queue;
    }
 	@Bean
 	public FanoutExchange fanoutExchange(){
 		FanoutExchange fanoutExchange=new FanoutExchange(Log.LOG_ACCESS_EXCHANGE);
 		return fanoutExchange;
 	} 
 	
 	@Bean
 	public Binding binding(){
 		Binding binding = BindingBuilder.bind(accessLogsQueue()).to(fanoutExchange());
 		return binding;
 	}
}
