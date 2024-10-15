package com.gaduationproject.cre8.externalApi.kafka;

import com.gaduationproject.cre8.app.chat.dto.response.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaSender {

    private final KafkaTemplate<String, MessageResponseDto> kafkaTemplate;

    public void send(String topic, MessageResponseDto data) {

        kafkaTemplate.send(topic, data);
    }
}

