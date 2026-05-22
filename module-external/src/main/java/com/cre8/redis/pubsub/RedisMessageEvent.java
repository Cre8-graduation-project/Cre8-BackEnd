package com.cre8.redis.pubsub;

import com.cre8.kafka.dto.MessageType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = RedisMessageEvent.RedisMessageEventBuilder.class)
public class RedisMessageEvent {

    private final Long senderId;
    private final String contents;
    private final LocalDateTime createdAt;
    private final MessageType messageType;
    private final Integer readCount;
    private final Long chattingRoomId;

    @JsonPOJOBuilder(withPrefix = "")
    public static class RedisMessageEventBuilder {
    }
}
