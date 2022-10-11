package com.ohj.rabbitmq.demo07;

import com.ohj.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

//消费者1
public class ReceiveLogs01 {
    public static final String EXCHANGE_NAME="direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //声明一个direct交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queue = channel.queueDeclare().getQueue();
        //绑定
        channel.queueBind(queue,EXCHANGE_NAME,"ohj1");
        DeliverCallback deliverCallback=( consumerTag,  message)->{
            System.out.println("消费者1："+new String(message.getBody()));
        };

        channel.basicConsume(queue,true,deliverCallback,consumerTag -> {});
    }
}
