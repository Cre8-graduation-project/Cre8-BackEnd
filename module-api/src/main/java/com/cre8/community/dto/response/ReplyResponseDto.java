package com.cre8.community.dto.response;

import com.cre8.community.entity.Reply;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyResponseDto {

    private Long replyId;
    private String contents;
    private Long memberId;
    private String memberNickName;
    private String memberAccessUrl;

    public static ReplyResponseDto of(final Reply reply){
        return new ReplyResponseDto(reply.getId(),
                reply.getContents(),
                reply.getWriter().getId(),
                reply.getWriter().getNickName(),
                reply.getWriter().getAccessUrl());
    }


}
