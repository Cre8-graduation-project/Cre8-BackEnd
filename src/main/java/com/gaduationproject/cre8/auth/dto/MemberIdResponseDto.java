package com.gaduationproject.cre8.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberIdResponseDto {

    private String loginId;

    private Long memberId;

    @Builder
    public MemberIdResponseDto(String loginId,Long memberId){

        this.loginId = loginId;
        this.memberId = memberId;
    }

}
