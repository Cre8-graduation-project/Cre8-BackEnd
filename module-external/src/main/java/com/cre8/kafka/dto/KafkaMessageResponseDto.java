package com.cre8.kafka.dto;

import com.cre8.mongodb.domain.ChattingMessage;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class KafkaMessageResponseDto {

    private Long senderId;
    private String contents;
    private LocalDateTime createdAt;
    private MessageType messageType;
    private Integer readCount;
    private Long chattingRoomId;

    public static KafkaMessageResponseDto ofChatMessage(final ChattingMessage chattingMessage){

        return new KafkaMessageResponseDto(chattingMessage.getSenderId(),
                chattingMessage.getContents(),
                chattingMessage.getCreatedAt(),
                MessageType.MESSAGE,
                chattingMessage.getReadCount(),
                chattingMessage.getChattingRoomId());
    }



}
