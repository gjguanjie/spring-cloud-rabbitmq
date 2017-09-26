package com.cloud.producer;

import com.alibaba.fastjson.JSON;
import com.cloud.exception.RabbitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MessageProducer implements RabbitTemplate.ConfirmCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProducer.class);

    @Autowired
    private RabbitTemplate messageTemplate;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.queue}")
    private String queue;

    @Value("${spring.rabbitmq.routingKey}")
    private String routeKey;

    public MessageProducer (RabbitTemplate messageTemplate) {
        messageTemplate.setConfirmCallback(this);
    }

    public void sendMessage(String msg){
        messageTemplate.convertAndSend(this.exchange,this.routeKey,msg,getCorrelationData());
    }

    public void sendMessage(Object msg){
        messageTemplate.convertAndSend(this.exchange,this.routeKey,convertMsgToString(msg),getCorrelationData());
    }

    public CorrelationData getCorrelationData(){
        return new CorrelationData(UUID.randomUUID().toString());
    }

    public String convertMsgToString(Object msg){
        return JSON.toJSONString(msg);
    }


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String msgId  = correlationData.getId();
        if (ack) {
            LOGGER.info("消息发送成功:" + msgId);
        } else {
            throw new RabbitException("rabbit send message occur error:" + cause);
        }
    }
}
