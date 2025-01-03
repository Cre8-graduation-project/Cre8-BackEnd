package com.cre8.kafka;

import com.cre8.kafka.dto.KafkaMessageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaSender {

    private final KafkaTemplate<String, KafkaMessageResponseDto> kafkaTemplate;

    public void send(String topic, KafkaMessageResponseDto data) {

        kafkaTemplate.send(topic, data);
    }
}

