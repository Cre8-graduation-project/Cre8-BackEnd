package com.gaduationproject.cre8.app.workfieldtag.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkFieldChildTagsSaveRequestDto {


    @Schema(description = "하위 태그의 이름 여러 개를 입력해주세요")
    private List<String> name = new ArrayList<>();

    @NotNull(message = "카테고리 아이디를 입력해주세요")
    @Schema(description = "카테고리를 지정합니다",example = "1")
    private Long workFieldSubCategoryId;

}
