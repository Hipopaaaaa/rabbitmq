package com.ohj.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author ohj
 * @Date 2022-10-06 00:13
 */

//生产者： 发消息
public class Producter {
    //队列名称
    public static final String QUEUE_NAME="hello";

    //发消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //连接RabbitMQ队列
        factory.setHost("47.100.199.241");
        //用户名，密码
        factory.setUsername("admin");
        factory.setPassword("admin");

        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        //使用默认的交换机，因此直接创建队列
        /**
         * 第一个参数：队列名称
         * 第二个参数：队列里的消息是否需要持久化，默认存储在内存中
         * 第三个参数：队列是否只供一个消费者进行消费，是否进行消息共享，false表示多个消费者消费
         * 第四个参数：是否自动删除，最后一个消费者断开连接后，该队列是否自动删除
         * 第五个参数： 其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //发消息
        String message="hello world";
        /**
         * 第一个参数： 发送到某个交换机
         * 第二个参数： 路由的key值
         * 第三个参数： 其他参数信息
         * 第四个参数： 发送的消息
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送完毕");

    }
}
