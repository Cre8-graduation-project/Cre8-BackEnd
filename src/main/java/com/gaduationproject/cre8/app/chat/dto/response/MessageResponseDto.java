package com.gaduationproject.cre8.app.chat.dto.response;

import com.gaduationproject.cre8.app.chat.dto.request.ChatDto;
import com.gaduationproject.cre8.externalApi.mongodb.domain.ChattingMessage;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageResponseDto {

    private Long senderId;
    private String contents;
    private LocalDateTime createdAt;

//    public static MessageResponseDto of(Message message){
//        return new MessageResponseDto(message.getSender().getId(),message.getContents());
//    }

    public static MessageResponseDto ofChatMessage(final ChattingMessage chattingMessage){
        return new MessageResponseDto(chattingMessage.getSenderId(),chattingMessage.getContents(),chattingMessage.getCreatedAt());
    }

    public static MessageResponseDto ofPayLoad(final Long memberId,final ChatDto chatDto,final LocalDateTime createdAt){
        return new MessageResponseDto(memberId,chatDto.getMessage(),createdAt);
    }

}
