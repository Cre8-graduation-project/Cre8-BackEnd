package com.gaduationproject.cre8.externalApi.kafka;

import com.gaduationproject.cre8.app.chat.dto.response.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageSender {

    private final KafkaTemplate<String, MessageResponseDto> kafkaTemplate;

    // 메시지를 지정한 Kafka 토픽으로 전송
    public void send(String topic, MessageResponseDto messageResponseDto) {

        // KafkaTemplate을 사용하여 메시지를 지정된 토픽으로 전송
        kafkaTemplate.send(topic, messageResponseDto);
    }
}
