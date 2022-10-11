package com.ohj.rabbitmq.demo09;

import com.ohj.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.util.HashMap;
import java.util.Map;

//消费者1
public class Consumer1 {
    //正常交换机
    public static final String NORMAL_EXCHANGE="normal_exchange";
    //死信交换机
    public static final String DEAD_EXCHANGE="dead_exchange";
    //正常队列
    public static final String NORMAL_QUEUE="normal_queue";
    //死信队列
    public static final String DEAD_QUEUE="dead_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtils.getChannel();
        //声明死信和正常交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //声明正常队列
        Map<String, Object> arguments=new HashMap<>();
        //正常队列设置死信交换机 DEAD_EXCHANGE
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死信交换机的routingKey为lisi
        arguments.put("x-dead-letter-routing-key","lisi");
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);


        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        //绑定正常交换机和正常队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        //绑定死信交换机和死信队列
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");


        DeliverCallback deliverCallback=( consumerTag,  message)->{
            System.out.println("Consumer1："+new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume(NORMAL_QUEUE,true,deliverCallback,consumerTag -> {});
    }
}
