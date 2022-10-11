package com.ohj.rabbitmq.demo03;

import com.ohj.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

//生产者
public class Task02 {

    public static final String ACK_QUEUE="test_ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //开启发布确认
        channel.confirmSelect();
        //声明队列
        channel.queueDeclare(ACK_QUEUE,true,false,false,null);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();

            //durable为true，也就是队列已经是持久化的
            channel.basicPublish("",ACK_QUEUE, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());

            System.out.println("生产者成功发送消息："+message);
        }
    }
}
