//package org.touear.starter.mq;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class SpringbootAmqpApplicationTests {
//
//    @Autowired
//    RabbitTemplate rabbitTemplate;
//
//    @Test
//    public void contextLoads() {
//
//        Map<String,Object> map = new HashMap<>();
//        map.put("msg","第一个消息");
//        map.put("data", Arrays.asList("helloworld",123,true));
//        //默认是采用Java的序列化机制
//        rabbitTemplate.convertAndSend("exchange.direct","atguigu.news",map);
//    }
//
//    @Test
//    public void receive(){
//        Object o = rabbitTemplate.receiveAndConvert("atguigu.news");
//        //class java.util.HashMap
//        System.out.println(o.getClass());
//        //{msg=第一个消息, data=[helloworld, 123, true]}
//        System.out.println(o);
//
//    }
//
//}
