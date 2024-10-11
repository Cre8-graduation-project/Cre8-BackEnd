package com.gaduationproject.cre8.app.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberIdResponseDto {

    private String loginId;

    private Long memberId;

    private String memberAccessUrl;

    private boolean isTMPPassword;

    @Builder
    public MemberIdResponseDto(String loginId,Long memberId,String memberAccessUrl,boolean isTMPPassword){

        this.loginId = loginId;
        this.memberId = memberId;
        this.memberAccessUrl = memberAccessUrl;
        this.isTMPPassword = isTMPPassword;
    }

}
