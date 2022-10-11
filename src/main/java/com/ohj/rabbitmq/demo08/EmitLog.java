package com.ohj.rabbitmq.demo08;

import com.ohj.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.sun.deploy.util.ArrayUtil;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

//生产者
public class EmitLog {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);


        List<String> routingKeys = Arrays.asList(
                "quick.orange.rabbit",
                "lazy.orange.elephant",
                "quick.orange.fox",
                "lazy.brown.fox",
                "lazy.pink.rabbit",
                "quick.brown.fox",
                "quick.orange.male.rabbit",
                "lazy.orange.male.rabbit"
        );


        //把routingkey作为消息进行发送
        routingKeys.forEach(routingKey->{
            try {
                channel.basicPublish(EXCHANGE_NAME, routingKey, null, routingKey.getBytes());
                System.out.println("消息发送成功。。。");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
