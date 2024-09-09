package com.gaduationproject.cre8.app.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplySaveRequestDto {

    @NotNull(message = "어느 게시글인지 아이디를 입력해주세요")
    @Schema(description = "댓글이 어떤 커뮤니티 게시글에 속하는지 입력",example = "1")
    private Long communityPostId;

    @Schema(description = "댓글이 대댓글이라면 그 댓글의 아이디, 댓글이 그냥 댓글이라면 null",example = "1")
    private Long parentReplyId;

    @NotEmpty(message = "내용을 입력해 주세요")
    @Size(max = 500, message = "200 글자 이내만 가능합니다.")
    @Schema(description = "게시물에 대한 댓글 내용을 입력합니다",example = "나는 진짜 능력이 있어요")
    private String contents;


}
