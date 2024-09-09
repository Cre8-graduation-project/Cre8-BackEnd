package com.gaduationproject.cre8.app.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyEditRequestDto {

    @NotNull(message = "수정할 댓글 아이디를 비울 수 는 없습니다")
    @Schema(description = "수정할 댓글 아이디 를 입력",example = "1")
    private Long replyId;

    @NotEmpty(message = "내용을 입력해 주세요")
    @Size(max = 500, message = "200 글자 이내만 가능합니다.")
    @Schema(description = "게시물에 대한 댓글 내용을 입력합니다",example = "나는 진짜 능력이 있어요")
    private String contents;

}
