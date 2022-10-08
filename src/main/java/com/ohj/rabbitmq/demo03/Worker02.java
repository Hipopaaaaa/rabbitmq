package com.ohj.rabbitmq.demo03;

import com.ohj.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//消费者2
public class Worker02 {
    public static final String ACK_QUEUE = "test_ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        //设置不公平分发
        channel.basicQos(1);
        DeliverCallback deliverCallback = (consumerTag, message)->{
            try {
                //睡眠30秒
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Worker02："+new String(message.getBody()));
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };

        boolean autoAck = false;
        channel.basicConsume(ACK_QUEUE, false, deliverCallback,consumerTag -> {
            System.out.println("消息处理出现异常");
        });
    }
}
