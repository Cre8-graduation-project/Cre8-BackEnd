package com.gaduationproject.cre8.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberIdResponseDto {

    private String userId;

    @Builder
    public MemberIdResponseDto(String userId){
        this.userId = userId;
    }

}
