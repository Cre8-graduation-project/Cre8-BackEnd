package com.cre8.community.dto.response;

import com.cre8.community.entity.CommunityBoard;
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
