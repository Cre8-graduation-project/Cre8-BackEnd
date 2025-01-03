package com.cre8.chat.config;


import com.cre8.chat.handler.StompErrorHandler;
import com.cre8.chat.handler.StompPreHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class StompConfig implements WebSocketMessageBrokerConfigurer {

    private final StompErrorHandler stompErrorHandler;
    private final StompPreHandler stompPreHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // stomp 접속 주소 url => /ws-stomp
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*");// 연결될 엔드포인트
    //     .withSockJS(); // SocketJS 를 연결한다는 설정

        registry.setErrorHandler(stompErrorHandler);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        ThreadPoolTaskScheduler te = new ThreadPoolTaskScheduler();
        te.setPoolSize(1);
        te.setThreadNamePrefix("wss-heartbeat-thread-");
        te.initialize();

//        // 메시지를 구독하는 요청 url => 즉 메시지 받을 때
        registry.enableSimpleBroker("/sub","/queue")
                .setTaskScheduler(te)
                .setHeartbeatValue(new long[]{20000,20000});

        registry.setApplicationDestinationPrefixes("/pub","/sub");


    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompPreHandler);
    }


}