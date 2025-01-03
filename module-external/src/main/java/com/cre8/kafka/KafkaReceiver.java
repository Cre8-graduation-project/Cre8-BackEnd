package com.cre8.kafka;

import com.cre8.kafka.dto.KafkaMessageResponseDto;
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
    public void receiveMessage(KafkaMessageResponseDto message) {

        simpMessageSendingOperations.convertAndSend("/sub/chat/room/" + message.getChattingRoomId(),message);
    }
}
