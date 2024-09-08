package com.gaduationproject.cre8.app.community.dto.response;

import com.gaduationproject.cre8.domain.community.entity.CommunityBoard;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityBoardResponseDto {

    private Long communityBoardId;
    private String communityBoardName;




    public static CommunityBoardResponseDto from(final CommunityBoard communityBoard) {
        return new CommunityBoardResponseDto(communityBoard.getId(),communityBoard.getName());
    }
}
