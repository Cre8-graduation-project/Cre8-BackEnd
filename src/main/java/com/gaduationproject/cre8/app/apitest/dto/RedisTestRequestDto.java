package com.gaduationproject.cre8.app.apitest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RedisTestRequestDto {

    @NotBlank(message = "저장할 아무 이름2을 선택하세요")
    @Schema(description = "아무 이름2", example = "진우2")
    @Size(min = 3, message = "최소 3이상이어야 합니다.")
    private String redisName;
}
