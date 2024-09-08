package com.gaduationproject.cre8.domain.community.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommunityPostSearchDBResponseDto {

    private Long communityPostId;
    private String title;
    private String writerNickName;
    private LocalDateTime createdAt;


    @Builder
    public CommunityPostSearchDBResponseDto(final Long communityPostId, final String title,
            final String writerNickName,final LocalDateTime createdAt) {
        this.communityPostId = communityPostId;
        this.title = title;
        this.writerNickName = writerNickName;
        this.createdAt = createdAt;
    }
}
