package com.cre8.chat.dto.response;


import com.cre8.chat.dto.request.ChatDto;
import com.cre8.mongodb.domain.ChattingMessage;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonDeserialize(builder = MessageResponseDto.MessageResponseDtoBuilder.class)
public class MessageResponseDto implements Serializable {

    private Long senderId;
    private String contents;
    private LocalDateTime createdAt;
    private MessageType messageType;
    private Integer readCount;
    private Long chattingRoomId;

//    public static MessageResponseDto of(Message message){
//        return new MessageResponseDto(message.getSender().getId(),message.getContents());
//    }

    public static MessageResponseDto ofChatMessage(final ChattingMessage chattingMessage){

        return new MessageResponseDto(chattingMessage.getSenderId(),
                                      chattingMessage.getContents(),
                                      chattingMessage.getCreatedAt(),
                                      MessageType.MESSAGE,
                                      chattingMessage.getReadCount(),
                                      chattingMessage.getChattingRoomId());
    }

    public static MessageResponseDto ofPayLoad(final Long memberId,final ChatDto chatDto,final LocalDateTime createdAt,final int readCount,
            final Long chatRoomId){

        return new MessageResponseDto(memberId,chatDto.getMessage(),createdAt,MessageType.MESSAGE,readCount,chatRoomId);
    }

    public static MessageResponseDto ofEnter(final String contents,final Long chattingRoomId){
        return new MessageResponseDto(null,contents,null,MessageType.ENTER,null,chattingRoomId);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class MessageResponseDtoBuilder {
    }

}
