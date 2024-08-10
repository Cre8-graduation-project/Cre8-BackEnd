package com.gaduationproject.cre8.app.workfieldtag.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkFieldTagSaveRequestDto {

    @NotEmpty(message = "상위 작업 분야 태그를 입력해 주세요")
    @Schema(description = "상위 작업 분야 태그 이름",example = "영상 편집")
    private String name;

}
