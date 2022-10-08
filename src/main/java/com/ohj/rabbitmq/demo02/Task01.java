package com.ohj.rabbitmq.demo02;

import com.ohj.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

public class Task01 {
    public static final String QUEUE_NAME="hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();




        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);




        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("发送消息完成。。。。");
        }
    }
}
