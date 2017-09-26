package com.cloud.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitProducerConfiguration {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.queue}")
    private String queue;

    @Value("${spring.rabbitmq.routingKey}")
    private String routeKey;

    @Value("${spring.rabbitmq.enableDurable}")
    private boolean enableDurable;

    @Value("${spring.rabbitmq.isPublisherConfirms}")
    private boolean isPublisherConfirms;

    @Bean
    @ConditionalOnMissingBean
    public Queue queue(){
        return new Queue(queue,enableDurable);
    }

    @Bean
    @ConditionalOnMissingBean
    public TopicExchange topicExchange(){
        return new TopicExchange(this.exchange,enableDurable,true);
    }

    /**
     * 关于
     *
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public Binding binding(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(routeKey);
    }

    @Bean
    public ConnectionFactory connectionFactory (){
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(this.host);
        factory.setPort(this.port);
        factory.setUsername(this.username);
        factory.setPassword(this.password);
        factory.setPublisherConfirms(this.isPublisherConfirms);
        return factory;
    }

    @Bean(value = "messageTemplate")
    public RabbitTemplate messageTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }
}
