package com.gaduationproject.cre8.api.chat.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatDto {


    @NotEmpty(message = "메시지의 값을 입력해주세요")
    private String message;

    @Builder
    public ChatDto(final String message) {

        this.message = message;
    }
}
