package com.gaduationproject.cre8.externalApi.kafka;

import com.gaduationproject.cre8.app.chat.dto.response.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaReceiver {

    private final SimpMessageSendingOperations simpMessageSendingOperations;


    @KafkaListener(topics = "chattest", containerFactory = "kafkaListenerContainerFactory")
    public void receiveMessage(MessageResponseDto message) {

        simpMessageSendingOperations.convertAndSend("/sub/chat/room/" + message.getChattingRoomId(),message);
    }
}
