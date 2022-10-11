package com.ohj.rabbitmq.demo07;

import com.ohj.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

//生产者
public class EmitLog {
    public static final String EXCHANGE_NAME="direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        Scanner scanner = new Scanner(System.in);

        System.out.println("输入要发送的队列的routingKey(ohj1、ohj2、ohj3)；");
        while (scanner.hasNext()){
            String routingKey = scanner.next();
            String message = scanner.next();
            //通过routingKey来指定发送给哪个队列
            channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes("UTF-8"));
            System.out.println("消息成功发出。。。");
        }
    }
}
