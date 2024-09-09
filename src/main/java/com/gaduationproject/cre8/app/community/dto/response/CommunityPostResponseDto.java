package com.gaduationproject.cre8.app.community.dto.response;

import com.gaduationproject.cre8.domain.community.entity.CommunityPost;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityPostResponseDto {

    private Long communityPostId;
    private String title;
    private String writerNickName;
    private int readCount;
    private LocalDateTime createdAt;
    private String contents;
    private int likeCounts;
    private boolean isLike;
    private List<ReplyListResponseDto> replyListResponseDtoList;

    public static CommunityPostResponseDto of (final CommunityPost communityPost,boolean isLike,
            final List<ReplyListResponseDto> replyListResponseDtoList) {


        return new CommunityPostResponseDto(communityPost.getId(),
                communityPost.getTitle(),
                communityPost.getWriter().getNickName(),
                communityPost.getReadCount(),
                communityPost.getCreatedAt(),
                communityPost.getContents(),
                communityPost.getLikeCount(),
                isLike,
                replyListResponseDtoList);
    }
}
