package com.gaduationproject.cre8.app.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "email 에 대한 인증번호가 맞는지 확인하기 위한 DTO")
public class EmailCheckAuthNumRequestDto {

    @Email(message = "이메일 형식에 맞지 않습니다")
    @NotBlank(message = "이메일을 입력해 주세요")
    @Schema(description = "가입하고자 하는 사용자의 이메일",example = "blackbox0209@naver.com")
    private String email;

    @NotBlank(message = "인증번호를 입력해 주세요")
    @Schema(description = "메일로 부터 받은 인증번호")
    private String authNum;

}
