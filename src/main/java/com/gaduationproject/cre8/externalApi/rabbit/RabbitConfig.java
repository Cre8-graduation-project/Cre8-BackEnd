package com.gaduationproject.cre8.externalApi.rabbit;

import java.util.concurrent.ExecutorService;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    private static final String CHAT_QUEUE_NAME = "chat.queue";
    private static final String CHAT_EXCHANGE_NAME = "chat.exchange";
    private static final String ROUTING_KEY = "room.*";

    @Value("${rabbit.host}")
    private String rabbitHost;

    @Value("${rabbit.id}")
    private String rabbitId;

    @Value("${rabbit.pw}")
    private String rabbitPw;

    //Queue 등록
    @Bean
    public AmqpAdmin amqpAdmin() {

        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
        rabbitAdmin.declareExchange(exchange());
        rabbitAdmin.declareQueue(chatQueue());
        rabbitAdmin.declareBinding(bindingKeroro(exchange(), chatQueue()));


        return rabbitAdmin;
    }

    //Exchange 등록
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(CHAT_EXCHANGE_NAME);
    }

    //Queue 등록
    @Bean
    public Queue chatQueue() {
        return new Queue(CHAT_QUEUE_NAME, true);
    }


    //Binding 등록
    @Bean
    public Binding bindingKeroro(DirectExchange exchange, Queue chatQueue) {
        return BindingBuilder.bind(chatQueue).to(exchange).with(ROUTING_KEY);
    }


    //RabbitTemplate을 사용하여 RabbitMQ 서버에 메시지를 전송할 수 있습니다.
    //RabbitTemplate은 RabbitMQ 서버에 접근하기 위한 클래스입니다.
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setExchange(CHAT_EXCHANGE_NAME);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    //ConnectionFactory 등록
    //ConnectionFactory는 RabbitMQ 서버에 접근하기 위한 클래스입니다.
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(rabbitHost);
        factory.setUsername(rabbitId);
        factory.setPassword(rabbitPw);


        return factory;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}

