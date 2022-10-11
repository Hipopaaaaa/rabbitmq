package com.ohj.rabbitmq.demo07;

import com.ohj.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

//消费者2
public class ReceiveLogs02 {
    public static final String EXCHANGE_NAME="direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queue = channel.queueDeclare().getQueue();
        //绑定两个routingKey
        channel.queueBind(queue,EXCHANGE_NAME,"ohj2");
        channel.queueBind(queue,EXCHANGE_NAME,"ohj3");

        DeliverCallback deliverCallback=( consumerTag,  message)->{
            System.out.println("消费者2："+new String(message.getBody()));
        };
        channel.basicConsume(queue,true,deliverCallback,consumerTag -> {});
    }
}
