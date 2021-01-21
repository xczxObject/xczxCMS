package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 入门程序
 */
public class Product02_publish {

    //队列
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";//email消息队列
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";//手机短信的队列
    private static final String EXCHANGE_FANOUT_INFORM="exchange_fanout_inform";//交换机队列

    public static void main(String[] args) {
        //通过连接工厂创建新的连接和mq建立连接
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);//端口
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //设置虚拟机
        //一个mq服务可以设置多个虚拟机，每个虚拟机就相当于一个独立的mq
        connectionFactory.setVirtualHost("/");

        Connection connection=null;
        Channel channel=null;
        try {
            //建立新连接
            connection=connectionFactory.newConnection();
            //创建会话通道，生产者和mq服务所有通信都在channel通道中完成
            channel=connection.createChannel();
            //声明队列
            //参数：String queue, boolean durable, boolean var3, boolean var4, Map<String, Object> var5
            /*** 
             * 声明队列，如果Rabbit中没有此队列将自动创建 
             * param1:队列名称
             * param2:是否持久化
             * param3:队列是否独占此连接
             * param4:队列不再使用时是否自动删除此队列
             * param5:队列参数
             */
            channel.queueDeclare(QUEUE_INFORM_EMAIL,true,false,false,null);
            channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);
            //声明一个交换机
            //参数：String exchange,String type
            /***参数明细
             * 1、交换机名称
             * 2、交换机类型，
             * fanout:对应的rabbitmq的工作模式是publish/subscribe、
             * topic:对应的topic工作模式
             * direct：对应的Routing工作模式
             * headers：对应的headers工作模式
             */
            //发布订阅模式
            channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM, BuiltinExchangeType.FANOUT);
            //交换机和队列绑定String queue, String exchange, String routingKey
            /***参数明细
              * 1、queue 队列名称
              * 2、exchange 交换机名称
              * 3、routingKey 路由key 作用是将交换机根据路由key的值将消息转发到指定的队列中，在发布订阅模式中调协为空字符串
             */
            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_FANOUT_INFORM,"");
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_FANOUT_INFORM,"");
            /*** 消息发布方法
             * param1：Exchange的名称，如果没有指定，则使用Default Exchange
             * param2:routingKey,消息的路由Key，是用于Exchange（交换机）将消息转发到指定的消息队列
             * param3:消息包含的属性
             * param4：消息体
             */
            for(int i=0;i<5;i++){
                String message="send inform message to user";
                channel.basicPublish(EXCHANGE_FANOUT_INFORM,"",null,message.getBytes());
                System.out.println("send to mq"+message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关闭连接
            //关闭通道
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
