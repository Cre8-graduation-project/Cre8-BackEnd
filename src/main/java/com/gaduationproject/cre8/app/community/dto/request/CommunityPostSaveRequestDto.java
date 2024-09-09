package com.gaduationproject.cre8.app.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityPostSaveRequestDto {

    @NotNull(message = "어느 게시판인지 아이디를 입력해주세요")
    @Schema(description = "커뮤니티 게시글이 어느 게시판에 속하는지 입력합니다",example = "1")
    private Long communityBoardId;

    @NotEmpty(message = "제목을 입력해 주세요")
    @Size(max = 50, message = "50 글자 이내만 가능합니다.")
    @Schema(description = "커뮤니티 게시글의 제목을 입력합니다",example = "이것은 게시글 제목 1,2,3,4,5,6")
    private String title;

    @NotEmpty(message = "내용을 입력해 주세요")
    @Size(max = 500, message = "500 글자 이내만 가능합니다.")
    @Schema(description = "구직자 게시물에 대한 내용을 입력합니다",example = "나는 진짜 능력이 있어요")
    private String contents;

    @Schema(description = "이미지 데이터")
    private MultipartFile multipartFile;

    @Builder
    public CommunityPostSaveRequestDto(final Long communityBoardId,final String title,
            final String contents, final MultipartFile multipartFile) {
        this.communityBoardId = communityBoardId;
        this.title = title;
        this.contents = contents;
        this.multipartFile = multipartFile;
    }
}
