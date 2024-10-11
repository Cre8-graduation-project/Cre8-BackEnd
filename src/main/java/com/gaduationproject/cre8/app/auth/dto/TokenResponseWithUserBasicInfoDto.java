package com.gaduationproject.cre8.app.auth.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseCookie;

@Getter
public class TokenResponseWithUserBasicInfoDto {
    private String grantType;
    private String accessToken;
    private ResponseCookie responseCookie;
    private Long accessTokenExpirationTime;
    private String loginId;
    private Long memberId;
    private String memberAccessUrl;
    private boolean isTMPPassword;

    @Builder
    public TokenResponseWithUserBasicInfoDto(String grantType,String accessToken,ResponseCookie responseCookie, Long accessTokenExpirationTime,
            String loginId,Long memberId,String memberAccessUrl,boolean isTMPPassword){
            this.grantType = grantType;
            this.accessToken = accessToken;
            this.responseCookie = responseCookie;
            this.accessTokenExpirationTime = accessTokenExpirationTime;
            this.loginId = loginId;
            this.memberId = memberId;
            this.memberAccessUrl = memberAccessUrl;
            this.isTMPPassword = isTMPPassword;
    }
}