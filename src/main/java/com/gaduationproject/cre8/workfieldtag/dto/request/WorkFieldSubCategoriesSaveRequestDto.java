package com.gaduationproject.cre8.workfieldtag.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkFieldSubCategoriesSaveRequestDto {

    @Schema(description = "상위 작업 분야의 카테고리를 여러 개 입력해주세요")
    private List<String> name = new ArrayList<>();


    @NotNull(message = "WorkFieldId 를 입력해주세요")
    @Schema(description = "작업 태그 아이디를 입력해주세요",example = "1")
    private Long workFieldId;

}
