package com.gaduationproject.cre8.adminapp.workfieldtag.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkFieldChildTagSaveRequestDto {

    @NotEmpty(message = "카테고리의 하위 태그를 입력해주세요")
    @Schema(description = "하위 태그의 이름",example = "Premier Pro")
    private String name;

    @NotNull(message = "카테고리 아이디를 입력해주세요")
    @Schema(description = "카테고리를 지정합니다",example = "1")
    private Long workFieldSubCategoryId;


}
