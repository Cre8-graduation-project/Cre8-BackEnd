package com.gaduationproject.cre8.app.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "임시 비밀번호 이후 새로운 비밀번호 설정을 위한 DTO")
public class PasswordChangeAfterTMPRequestDto {

    @NotBlank(message = "비밀번호는 공백이 될 수 없습니다")
    @Size(min=5,max = 20 , message = "최소 5글자에서 최대 20글자를 입력해주세요")
    @Schema(description = "사용자의 새로운 비밀번호",example = "kdkdkdk",maxLength = 20,minLength = 5)
    private String newPassword;

}
