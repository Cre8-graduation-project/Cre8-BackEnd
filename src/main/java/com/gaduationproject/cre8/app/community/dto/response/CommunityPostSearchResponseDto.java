package com.gaduationproject.cre8.app.community.dto.response;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityPostSearchResponseDto {

    private Long communityPostId;
    private String title;
    private int replyCount;
    private String writerNickName;
    private LocalDateTime createdAt;


    public static CommunityPostSearchResponseDto of(final Long communityPostId,final String title, int replyCount,
            final String writerNickName, final LocalDateTime createdAt) {

        return new CommunityPostSearchResponseDto(communityPostId,title,replyCount,writerNickName,createdAt);
    }
}
