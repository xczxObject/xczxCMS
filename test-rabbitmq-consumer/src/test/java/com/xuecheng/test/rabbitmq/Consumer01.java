package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 入门程序消费者
 */
public class Consumer01 {

    //队列
    private static final String QUEUE="helloword2";

    public static void main(String[] args) throws IOException, TimeoutException {
        //通过连接工厂创建新的连接和mq建立连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);//端口
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //设置虚拟机
        //一个mq服务可以设置多个虚拟机，每个虚拟机就相当于一个独立的mq
        connectionFactory.setVirtualHost("/");

        Connection connection=connectionFactory.newConnection();
        //创建会话通道，生产者和mq服务所有通信都在channel通道中完成
        Channel channel=connection.createChannel();

        /***
         * 声明队列，如果Rabbit中没有此队列将自动创建
         * param1:队列名称
         * param2:是否持久化
         * param3:队列是否独占此连接
         * param4:队列不再使用时是否自动删除此队列
         * param5:队列参数
         */
        channel.queueDeclare(QUEUE,true,false,false,null);

        //消费方法
        DefaultConsumer defaultConsumer=new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //交换机
                String exchange=envelope.getExchange();
                //消息id
                long deliveryTag=envelope.getDeliveryTag();
                //消息内容
                String message=new String(body,"utf-8");
                System.out.println("receive meaage:"+message);
            }
        };

        //监听队列
        //参数：String queue,boolean autoAck,Consumer callback
        /**
         * 参数明细：
         * 1.queue 队列名称
         * 2.autoAck 自动回复，是否自动回复，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置 为false则需要手动回复
         * 3.callback 消费方法 消费者接收到消息后调用此方法
         */
        channel.basicConsume(QUEUE,true,defaultConsumer);
    }
}
