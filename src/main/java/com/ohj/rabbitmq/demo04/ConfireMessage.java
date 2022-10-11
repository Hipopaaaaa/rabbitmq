package com.ohj.rabbitmq.demo04;

import com.ohj.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.DeliverCallback;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

//比较三种发布确认的速度
public class ConfireMessage  {

    //批量发消息
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        //单个确认发布
        //ConfireMessage.publishSingle();
        //批量发布确认
        //ConfireMessage.publishBatch();
        //异步发布确认
        ConfireMessage.publishAsync();
    }

    //单个确认发布
    public static void publishSingle() throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            //消息确认发布后的回调
            boolean flag = channel.waitForConfirms();
//            if (flag) {
//                System.out.println("消息: < " + i + " > 发布成功");
//            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("批量发布 " + MESSAGE_COUNT + "条消息的总用时：" + (end - begin));
    }


    //批量发布确认
    public static void publishBatch() throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量确认消息的个数
        int batchSize = 100;

        //批量发送消息，并批量发布确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            //达到100条消息时，批量确认一次
            if (i % batchSize == 0) {
                //发布确认
                channel.waitForConfirms();
                System.out.println("有< " + i + " >条消息发布成功");
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("批量发布 " + MESSAGE_COUNT + "条消息的总用时：" + (end - begin));
    }


    //异步发布确认
    public static void publishAsync() throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();


        /**
         * 线程安全有序的一个哈希表，适用于高并发的情况
         * 1.轻松的将序号与消息进行关联
         * 2.轻松批量删除消息
         * 3.支持高并发
         */
        ConcurrentSkipListMap<Long,String> concurrentSkipListMap=new ConcurrentSkipListMap<>();

        //消息确认成功的回调函数
        /**
         * 第一个参数： 消息的标记
         * 第二个参数： 是否为批量确认
         */
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            //2.删除已经确认的消息，剩下的就是未确认的消息
            if(multiple){
                //批量删除
                ConcurrentNavigableMap<Long, String> confirmed = concurrentSkipListMap.headMap(deliveryTag);
                confirmed.clear();  //confirmed的清空操作会影响到 concurrentSkipListMap
            }else {
                concurrentSkipListMap.remove(deliveryTag);
            }

            System.out.println("确认的消息:"+deliveryTag);
        };

        //消息确认失败的回调函数
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            //3.打印一下未确认的消息
            String message = concurrentSkipListMap.get(deliveryTag);
            System.out.println("未确认的消息:"+message+" ，编号："+deliveryTag);
        };



        //准备消息的监听器，监听哪些消息成功了，哪些消息失败了
        /**
         * 第一个参数： 监听哪些消息发布成功
         * 第二个参数： 监听哪些消息发布失败
         */
        channel.addConfirmListener(ackCallback,nackCallback); //异步通知
        //批量发布消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            //1.此处记录下所有要发送的消息
            concurrentSkipListMap.put(channel.getNextPublishSeqNo(),message);
        }


        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("批量发布 " + MESSAGE_COUNT + "条消息的总用时：" + (end - begin));
    }


}
