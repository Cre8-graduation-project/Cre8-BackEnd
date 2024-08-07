package com.gaduationproject.cre8.api.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginIdCheckResponseDto {

    private boolean loginIdChecked;

    @Builder
    public LoginIdCheckResponseDto(boolean loginIdChecked){
        this.loginIdChecked= loginIdChecked;
    }

}
