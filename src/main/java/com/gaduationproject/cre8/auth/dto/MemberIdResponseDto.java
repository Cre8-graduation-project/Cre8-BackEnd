package com.gaduationproject.cre8.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberIdResponseDto {

    private Long userId;

    @Builder
    public MemberIdResponseDto(Long userId){
        this.userId = userId;
    }

}
