package com.gaduationproject.cre8.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailCheckRequestDto {

    @Email(message = "이메일 형식에 맞지 않습니다")
    @NotBlank(message = "이메일을 입력해 주세요")
    private String email;

    @NotBlank(message = "인증번호를 입력해 주세요")
    private String authNum;

}
