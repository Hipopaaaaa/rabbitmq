package com.ohj.rabbitmq.demo05;

import com.ohj.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

public class Task3 {
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtils.getChannel();

        String queue = channel.queueDeclare().getQueue();
        String message="临时队列";
        channel.basicPublish("",queue,null,message.getBytes(StandardCharsets.UTF_8));
    }
}
