package com.ohj.rabbitmq.demo06;

import com.ohj.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

//消费者1
public class ReceiveLogs01 {

    //交换机名
    public static final String EXCHANGE_NAME="logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtils.getChannel();

        //声明一个临时队列
        String queue = channel.queueDeclare().getQueue();
        //绑定交换机与队列
        channel.queueBind(queue,EXCHANGE_NAME,"ohj1");
        System.out.println("等待接收消息...");

        DeliverCallback deliverCallback=(consumerTag,  message)->{
            System.out.println("01接收到："+new String(message.getBody()));
        };
        channel.basicConsume(queue,true,deliverCallback,consumerTag -> {
            System.out.println("消息处理失败");
        });
    }
}
