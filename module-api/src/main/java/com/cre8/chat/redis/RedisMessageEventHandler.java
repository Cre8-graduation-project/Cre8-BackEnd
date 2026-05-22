package com.cre8.chat.redis;

import com.cre8.chat.dto.response.MessageResponseDto;
import com.cre8.chat.dto.response.MessageType;
import com.cre8.redis.pubsub.RedisMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisMessageEventHandler {

    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleRedisMessage(RedisMessageEvent event) {
        MessageResponseDto dto = MessageResponseDto.builder()
                .senderId(event.getSenderId())
                .contents(event.getContents())
                .createdAt(event.getCreatedAt())
                .messageType(MessageType.valueOf(event.getMessageType().name()))
                .readCount(event.getReadCount())
                .chattingRoomId(event.getChattingRoomId())
                .build();

        messagingTemplate.convertAndSend("/sub/chat/room/" + dto.getChattingRoomId(), dto);
    }
}
