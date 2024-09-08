package com.gaduationproject.cre8.app.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityPostEditRequestDto {

    @NotNull(message = "수정할 communityPostID 를 입력해주세요")
    @Schema(description = "수정할 커뮤니티  게시글 아이디",example = "1")
    private Long communityPostId;

    @NotEmpty(message = "제목을 입력해 주세요")
    @Schema(description = "커뮤니티 게시글의 제목을 입력합니다",example = "이것은 게시글 제목 1,2,3,4,5,6")
    @Size(max = 50,message = "50글자 미만으로 입력해주세요")
    private String title;

    @NotEmpty(message = "내용을 입력해 주세요")
    @Schema(description = "구직자 게시물에 대한 내용을 입력합니다",example = "나는 진짜 능력이 있어요")
    @Size(max = 50,message = "500글자 미만으로 입력해주세요")
    private String contents;

    @Schema(description = "이미지 데이터")
    private MultipartFile multipartFile;


    @Builder
    public CommunityPostEditRequestDto(
            final Long communityPostId, final String title,
            final String contents, final MultipartFile multipartFile) {

        this.communityPostId = communityPostId;
        this.title = title;
        this.contents = contents;
        this.multipartFile = multipartFile;
    }
}
