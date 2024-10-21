package com.gaduationproject.cre8.app.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "로그인 아이디를 기반으로 요청 시 임시비밀번호를 가입 이메일로 전송하는 DTO")
public class LoginIdRequestDto {

    @NotBlank(message = "가입한 로그인 아이디를 입력해주세요")
    @Schema(description = "로그인 아이디",example = "dionisos198")
    private String loginId;

    @NotBlank(message = "사용자의 이름을 입력해주세요")
    @Schema(description = "사용자 이름",example = "이진우")
    private String name;

    @Email(message = "이메일 형식에 맞지 않습니다")
    @NotEmpty(message = "이메일을 입력해 주세요")
    @Schema(description = "사용자의 이메일",example = "dionisos198@naver.com")
    private String email;


}
