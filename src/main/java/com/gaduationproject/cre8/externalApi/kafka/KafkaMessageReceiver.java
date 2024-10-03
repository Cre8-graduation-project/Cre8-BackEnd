package com.gaduationproject.cre8.externalApi.kafka;

import com.gaduationproject.cre8.app.chat.dto.response.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageReceiver {

    private final SimpMessageSendingOperations template;


    @KafkaListener(topics = "devchat", containerFactory = "kafkaListenerContainerFactory")
    public void receiveMessage(MessageResponseDto messageResponseDto) {
        log.info("전송 위치 = /subscribe/public/"+ messageResponseDto.getChattingRoomId());
        log.info("채팅 방으로 메시지 전송 = {}", messageResponseDto);

        // 메시지객체 내부의 채팅방번호를 참조하여, 해당 채팅방 구독자에게 메시지를 발송한다.
        template.convertAndSend("/sub/chat/room/" + messageResponseDto.getChattingRoomId(), messageResponseDto);
    }
}

