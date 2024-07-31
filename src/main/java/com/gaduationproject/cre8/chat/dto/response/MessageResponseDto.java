package com.gaduationproject.cre8.chat.dto.response;

import com.gaduationproject.cre8.chat.entity.Message;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageResponseDto {

    private Long senderId;
    private String contents;

    public static MessageResponseDto of(Message message){
        return new MessageResponseDto(message.getSender().getId(),message.getContents());
    }

}
