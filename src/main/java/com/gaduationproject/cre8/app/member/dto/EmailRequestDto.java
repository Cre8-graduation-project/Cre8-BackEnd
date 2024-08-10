package com.gaduationproject.cre8.app.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "email 에 대한 인증번호를 전송하고 중복되는 email 이 이미 존재하는지 확인하는 dto")
public class EmailRequestDto {

    @Email(message = "이메일 형식에 맞지 않습니다")
    @NotBlank(message = "이메일을 입력해 주세요")
    @Schema(description = "사용자가 가입하고자 하는 이메일",example = "blackbox0209@naver.com")
    private String email;
}