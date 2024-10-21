package com.gaduationproject.cre8.app.chat.handler;

import com.gaduationproject.cre8.app.chat.event.SessionSubscribedEvent;
import com.gaduationproject.cre8.app.chat.service.ChattingService;
import com.gaduationproject.cre8.app.chat.service.MessagingService;
import com.gaduationproject.cre8.externalApi.redis.service.ChattingRoomConnectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final ChattingRoomConnectService chattingRoomConnectService;
    private final ChattingService chattingService;
    private static final String SUB = "SUB";

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {

        log.info("connection 생성");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        chattingRoomConnectService.disconnectChattingRoom(headerAccessor.getSessionId());
        log.info("disconnect 끊김");
    }


    @EventListener
    public void handleWebSocketUnsubscribeListener(SessionUnsubscribeEvent event) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        final Long chattingRoomId = Long.valueOf(headerAccessor.getSubscriptionId());

        chattingRoomConnectService.disconnectChattingRoom(headerAccessor.getSessionId());
        log.info("채팅방 퇴장: chattingRoomId: {}",chattingRoomId);

    }

    @EventListener
    public void handleWebSocketAfterSubscribe(SessionSubscribedEvent event){

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        final Long chattingRoomId = Long.valueOf(headerAccessor.getSubscriptionId());
        final String loginId = headerAccessor.getUser().getName();

        chattingRoomConnectService.connectChattingRoom(chattingRoomId,loginId,headerAccessor.getSessionId());
        chattingService.sendEnterMessage(chattingRoomId,loginId);
        chattingService.updateCountAllZero(chattingRoomId,loginId);

        headerAccessor.getSessionAttributes().put(SUB,chattingRoomId);


        log.info("채팅방 입장: chattingRoomId: {}",chattingRoomId);


    }



}
