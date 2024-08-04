package com.gaduationproject.cre8.chat.dto.response;

import com.gaduationproject.cre8.chat.entity.ChattingRoom;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingRoomResponseDto {

    private Long roomId;

    private String nickName;

    private String latestMessage;

    @Builder
    public ChattingRoomResponseDto(Long roomId, String nickName, String latestMessage) {
        this.roomId = roomId;
        this.nickName = nickName;
        this.latestMessage = latestMessage;
    }
}
