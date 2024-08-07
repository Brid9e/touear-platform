package com.touear.common.constant;

/**
 * @Title: RabbitConstant
 * @Description: rabbitMq常数类，补充常数时说明，尽量将同一个模块中的rabbit常数封装为一个内部类，方便引用
 * 起名规范：主要分为四部分，每部分用"."号连接
 * 第一部分：touear               【框架名，固定不变】
 * 第二部分：业务项目名称            【如消息推送为：message】
 * 第三部分：队列/交换机功能简称     【如消息发送为：send】
 * 第四部分：队列和交换机的区分      【队列使用queue，交换机使用exchange】
 * @date 2020-08-20 09:35:27
 */
public class RabbitConstant {

    /**
     * 消息推送
     */
    public static class Message {
        /**
         * 消息发送队列名称
         */
        public static final String MESSAGE_SEND_QUEUE = "touear.message.send.queue";
        /**
         * 消息对内发送交换机名称
         */
        public static final String MESSAGE_INTERNAL_SEND_EXCHANGE = "touear.message.internal.send.exchange";
        /**
         * webSocket发送消息的交换机名称
         */
        public static final String MESSAGE_SEND_WEBSOCKET_EXCHANGE = "touear.message.websocket.send.exchange";
        /**
         * 消息对内发送队列名称
         */
        public static final String MESSAGE_INTERNAL_SEND_QUEUE = "touear.message.internal.send.queue";
        /**
         * 消息对内持久化队列名称
         */
        public static final String MESSAGE_INTERNAL_DB_QUEUE = "touear.message.internal.db.queue";
       
        /**
         * 清除未读消息
         */
        public static final String MESSAGE_SET_READ = "touear.message.set.read";
    }

    /**
     * @Title: RabbitConstant.java
     * @Description: 日志
     */
    public static class Log {
        /**
         * 网关请求日志
         */
        public static final String LOG_ACCESS_QUEUE = "touear.log.access.queue";
        /**
         * 应用点击日志
         */
        public static final String LOG_CLICK_QUEUE = "touear.log.click.queue";
        /**
         * 登录日志
         */
        public static final String LOG_LOGIN_QUEUE = "touear.log.login.queue";

        /**
         * 操作日志
         */
        public static final String LOG_OPLOG_QUEUE = "touear.log.oplog.queue";
        /**
         * 更新登录日志
         */
        public static final String LOG_LOGIN_UPDATE_QUEUE = "touear.log.login.update.queue";
        /**
         * 应用点击日志交换机名称
         */
        public static final String LOG_CLICK_EXCHANGE = "touear.log.click.exchange";
        /**
         * 网关请求日志交换机名称
         */
        public static final String LOG_ACCESS_EXCHANGE = "touear.log.access.exchange";
        /**
         * PUBLICKEY
         */
        public static final String PUBLICKEY = "touear";
    }


    /**
     * 登录绑定
     * @author wangqq
     * @date 2021年6月11日 下午1:11:27
     * @version 1.0
     */
    public static class Bind {
    	/**
    	 * 异步绑定
    	 */
    	public static final String LOGIN_BIND = "touear.login.bind";
    }



}
