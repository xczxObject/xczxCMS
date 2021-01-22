package com.xuecheng.rabbitmq.mq;

import com.rabbitmq.client.Channel;
import com.xuecheng.rabbitmq.config.RabbitmqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiveHandler {

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_EMAIL})
    public void send_eamil(String msg, Message message, Channel channel)
    {
        System.out.println("reccivc message is:"+msg);
    }

}
