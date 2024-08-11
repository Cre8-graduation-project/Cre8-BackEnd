package com.gaduationproject.cre8.adminapp.workfieldtag.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkFieldSubCategorySaveRequestDto {

    @NotEmpty(message = "상위 작업 분야의 카테고리를 입력해주세요")
    @Schema(description = "상위 작업 분야의 카테고리를 입력해주세요",example = "작업 도구")
    private String name;


    @NotNull(message = "WorkFieldId 를 입력해주세요")
    @Schema(description = "작업 태그 아이디를 입력해주세요",example = "1")
    private Long workFieldId;

}
