package com.ohj.rabbitmq.demo09;

import com.ohj.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.impl.AMQBasicProperties;

import java.util.Scanner;

public class Producer {

    //正常交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtils.getChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);

        //设置死信消息，ttl为 10s
        AMQP.BasicProperties props = new AMQP.BasicProperties().builder().expiration("100000").build();
        for (int i = 0; i < 11; i++) {
            String message = i + "";
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", props, message.getBytes("UTF-8"));
            System.out.println("消息发送成功");
        }
    }
}
