package com.gaduationproject.cre8.app.chat.dto.response;

import com.gaduationproject.cre8.domain.member.entity.Member;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingRoomInfoResponseDto {

    private String opponentNickName;
    private String opponentAccessUrl;
    private List<MessageResponseDto> messageResponseDtoList;
    private boolean hasNextPage;


    public static ChattingRoomInfoResponseDto of(final Member opponent,
            final List<MessageResponseDto> messageResponseDtoList,boolean hasNextPage) {

        return new ChattingRoomInfoResponseDto(opponent.getNickName(),opponent.getAccessUrl(),messageResponseDtoList,hasNextPage);
    }
}
