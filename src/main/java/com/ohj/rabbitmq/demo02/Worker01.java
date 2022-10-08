package com.ohj.rabbitmq.demo02;

import com.ohj.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Worker01 implements Runnable {

    public static final String QUEUE_NAME="hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //开启两个线程
        Worker01 Worker01 = new Worker01();
        Thread thread1 = new Thread(Worker01);
        thread1.start();

        Thread thread2 = new Thread(Worker01);
        thread2.start();
    }

    @Override
    public void run() {

        String threadName=Thread.currentThread().getName();
        while (true){
            try {
                Channel channel = RabbitMQUtils.getChannel();
                DeliverCallback deliverCallback=(consumerTag, message)->{
                    System.out.println(threadName+"消费:"+new String(message.getBody()));
                };
                CancelCallback cancelCallback=(consumerTag)->{
                    System.out.println("消息传递失败");
                };
                channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
