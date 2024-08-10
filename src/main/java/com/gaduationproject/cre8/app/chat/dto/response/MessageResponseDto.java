package com.gaduationproject.cre8.app.chat.dto.response;

import com.gaduationproject.cre8.app.chat.dto.request.ChatDto;
import com.gaduationproject.cre8.domain.chat.entity.Message;
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

    public static MessageResponseDto of(Message message){
        return new MessageResponseDto(message.getSender().getId(),message.getContents());
    }

    public static MessageResponseDto ofPayLoad(final Long memberId,final ChatDto chatDto){
        return new MessageResponseDto(memberId,chatDto.getMessage());
    }

}
