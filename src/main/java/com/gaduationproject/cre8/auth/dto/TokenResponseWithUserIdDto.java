package com.gaduationproject.cre8.auth.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseCookie;

@Getter
public class TokenResponseWithUserIdDto {
    private String grantType;
    private String accessToken;
    private ResponseCookie responseCookie;
    private Long accessTokenExpirationTime;
    private String loginId;
    private Long memberId;

    @Builder
    public TokenResponseWithUserIdDto(String grantType,String accessToken,ResponseCookie responseCookie, Long accessTokenExpirationTime,
            String loginId,Long memberId){
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.responseCookie = responseCookie;
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.loginId = loginId;
        this.memberId = memberId;
    }
}