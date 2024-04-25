package com.gaduationproject.cre8.apitest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "Test 를 저장하기 위한 DTO")
public class TestRequestDto {

    @NotBlank(message = "저장할 아무 이름을 선택하세요")
    @Schema(description = "아무 이름", example = "진우")
    @Size(min = 5, message = "최소 5이상이어야 합니다.")
    private String name;

}
