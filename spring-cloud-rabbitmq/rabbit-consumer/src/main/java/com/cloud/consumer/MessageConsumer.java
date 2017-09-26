package com.cloud.consumer;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

//@RabbitListener(queues = "test-1")
@Component
public class MessageConsumer implements ChannelAwareMessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    //@RabbitHandler
    public void recieveMessage(@Payload String msg){
        LOGGER.info("消息消息：" + msg);
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        byte [] msgBody = message.getBody();
        LOGGER.info("监听消息为：" + new String(msgBody));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
