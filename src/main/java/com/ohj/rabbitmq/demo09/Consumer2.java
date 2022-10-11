package com.ohj.rabbitmq.demo09;

import com.ohj.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class Consumer2 {

    //死信交换机
    public static final String DEAD_EXCHANGE="dead_exchange";
    //死信队列
    public static final String DEAD_QUEUE="dead_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtils.getChannel();

        //因为交换机和队列在Consumer1中已经声明完了且绑定完了，Consumer2只需要接收消息即可
        DeliverCallback deliverCallback=(consumerTag, message)->{
            System.out.println("Consumer2："+new String(message.getBody(),"UTF-8"));
        };

        channel.basicConsume(DEAD_QUEUE,true,deliverCallback,consumerTag -> {});
    }
}
