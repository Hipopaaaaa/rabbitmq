package com.ohj.rabbitmq.demo03;

import com.ohj.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//消费者1
public class Worker01 {
    public static final String ACK_QUEUE = "test_ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        channel.basicQos(1);
        DeliverCallback deliverCallback = (consumerTag, message)->{
            //睡眠1秒
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Worker01："+new String(message.getBody()));
            //手动应答
            /**
             * 第一个参数：消息的标记，tag
             * 第二个参数：批量处理
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        //手动应答
        boolean autoAck = false;
        channel.basicConsume(ACK_QUEUE, autoAck,deliverCallback,consumerTag -> {
            System.out.println("消息处理出现异常");
        } );
    }
}
