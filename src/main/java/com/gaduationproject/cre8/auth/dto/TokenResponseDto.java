package com.gaduationproject.cre8.auth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;

@Getter
public class TokenResponseDto {
    private String grantType;
    private String accessToken;
    //private ResponseCookie responseCookie;
    private String refreshToken;
    private Long accessTokenExpirationTime;

    @Builder
    public TokenResponseDto(String grantType,String accessToken,String refreshToken, Long accessTokenExpirationTime){
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpirationTime = accessTokenExpirationTime;
    }
}