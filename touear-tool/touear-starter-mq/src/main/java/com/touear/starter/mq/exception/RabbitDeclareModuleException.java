package com.touear.starter.mq.exception;

/**
 * @Title: RabbitDeclareModuleException
 * @Description: rabbitMq初始化异常
 * @author wangqq
 * @date 2022-06-15 17:18:21
 * @version 1.0
 */
public class RabbitDeclareModuleException extends Exception {

    public RabbitDeclareModuleException() {
        super();
    }

    public RabbitDeclareModuleException(String message) {
        super(message);
    }

}
