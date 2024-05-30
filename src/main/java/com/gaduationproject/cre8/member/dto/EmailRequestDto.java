package com.gaduationproject.cre8.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailRequestDto {

    @Email(message = "이메일 형식에 맞지 않습니다")
    @NotBlank(message = "이메일을 입력해 주세요")
    private String email;
}