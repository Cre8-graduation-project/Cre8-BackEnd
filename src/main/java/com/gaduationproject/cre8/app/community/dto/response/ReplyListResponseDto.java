package com.gaduationproject.cre8.app.community.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyListResponseDto {

    private ReplyResponseDto parentReplyResponseDto;
    private List<ReplyResponseDto> childReplyResponseDto;


    public static ReplyListResponseDto of (final ReplyResponseDto parentReplyResponseDto,
            final List<ReplyResponseDto> childReplyResponseDto) {

        return new ReplyListResponseDto(parentReplyResponseDto,childReplyResponseDto);
    }
}
