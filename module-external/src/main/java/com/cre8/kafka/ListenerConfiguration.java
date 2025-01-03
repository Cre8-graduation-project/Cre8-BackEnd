package com.cre8.kafka;

import com.amazonaws.util.ImmutableMapParameter;
import com.cre8.kafka.dto.KafkaMessageResponseDto;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
public class ListenerConfiguration {

    @Value("${kafka.host}")
    private String kafkaHost;

    // KafkaListener 컨테이너 팩토리를 생성하는 Bean 메서드
    @Bean
    ConcurrentKafkaListenerContainerFactory<String, KafkaMessageResponseDto> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaMessageResponseDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    // Kafka ConsumerFactory를 생성하는 Bean 메서드
    @Bean
    public ConsumerFactory<String, KafkaMessageResponseDto> consumerFactory() {

        JsonDeserializer<KafkaMessageResponseDto> deserializer = new JsonDeserializer<>();
        deserializer.addTrustedPackages("*");

        Map<String, Object> consumerConfigurations =
                ImmutableMapParameter.<String, Object> builder()
                        .put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost)
                        .put(ConsumerConfig.GROUP_ID_CONFIG, "chat")
                        .put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
                        .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer)
                        .put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
                        .build();

        return new DefaultKafkaConsumerFactory<>(consumerConfigurations, new StringDeserializer(), deserializer);
    }

}
