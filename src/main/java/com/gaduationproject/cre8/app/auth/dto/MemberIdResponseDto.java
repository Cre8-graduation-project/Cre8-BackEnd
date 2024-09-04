package com.gaduationproject.cre8.app.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberIdResponseDto {

    private String loginId;

    private Long memberId;

    private String memberAccessUrl;

    @Builder
    public MemberIdResponseDto(String loginId,Long memberId,String memberAccessUrl){

        this.loginId = loginId;
        this.memberId = memberId;
        this.memberAccessUrl = memberAccessUrl;
    }

}
