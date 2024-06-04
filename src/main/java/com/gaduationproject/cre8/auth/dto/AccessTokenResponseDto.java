package com.gaduationproject.cre8.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AccessTokenResponseDto {
    private String accessToken;

    @Builder
    public AccessTokenResponseDto(String accessToken){
        this.accessToken = accessToken;

    }

}

