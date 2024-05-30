package com.gaduationproject.cre8.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailCheckResponseDto {

    private boolean emailChecked;

    @Builder
    public EmailCheckResponseDto(boolean emailChecked){
        this.emailChecked = emailChecked;
    }
}
