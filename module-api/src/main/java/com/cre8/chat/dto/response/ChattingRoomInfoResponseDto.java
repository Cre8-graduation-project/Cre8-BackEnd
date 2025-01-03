package com.cre8.chat.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingRoomInfoResponseDto {


    private List<MessageResponseDto> messageResponseDtoList;
    private boolean hasNextPage;


    public static ChattingRoomInfoResponseDto of(final List<MessageResponseDto> messageResponseDtoList,boolean hasNextPage) {

        return new ChattingRoomInfoResponseDto(messageResponseDtoList,hasNextPage);
    }
}
