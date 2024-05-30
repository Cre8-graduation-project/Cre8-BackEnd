package com.gaduationproject.cre8.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginIdCheckRequestDto {

    @NotBlank(message = "아이디는 공백이 될 수 없습니다")
    @Size(max = 20, message = "최대 20 글자 미만으로 만들어주세요!")
    private String loginId;

}
