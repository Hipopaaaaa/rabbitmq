package com.ohj.rabbitmq.demo01;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author ohj
 * @Date 2022-10-06 13:29
 */

//消费者，用于接收消息的
public class Consumer {
    public static final String QUEUE_NAME="hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.100.199.241");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        DeliverCallback deliverCallback=(consumerTag,message)->{
            System.out.println(new String(message.getBody()));
        };
        CancelCallback cancelCallback=consumerTag->{
            System.out.println("消息消费被中断");
        };
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);

    }
}
