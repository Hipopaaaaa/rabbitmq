package com.ohj.rabbitmq.demo08;

import com.ohj.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

//消费者2
public class ReceiveLogs02 {
    public static final String EXCHANGE_NAME="topic_logs";

    public static void main(String[] args)  throws  Exception{
        Channel channel = RabbitMQUtils.getChannel();

        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        String queue = channel.queueDeclare().getQueue();
        //绑定
        channel.queueBind(queue,EXCHANGE_NAME,"*.*.rabbit");
        channel.queueBind(queue,EXCHANGE_NAME,"lazy.#");


        DeliverCallback deliverCallback=(consumerTag, message)->{
            System.out.println("消费者2："+" 接收队列： "+queue+" 绑定键："+message.getEnvelope().getRoutingKey()
                    +"消息内容： "+new String(message.getBody()));
        };
        channel.basicConsume(queue,true,deliverCallback,consumerTag -> {});
    }
}
