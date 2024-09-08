package com.gaduationproject.cre8.app.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityBoardSaveRequestDto {

    @NotBlank(message = "생성하고자 하는 커뮤니티 이름을 입력해주세요")
    @Schema(description = "생성하고자 하는 커뮤니티 이름 ",example = "자유게시판")
    private String name;

}
