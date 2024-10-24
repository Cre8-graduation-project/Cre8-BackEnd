package com.gaduationproject.cre8.app.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailCheckAuthNumResponseDto {

    private boolean emailChecked;

    @Builder
    public EmailCheckAuthNumResponseDto(boolean emailChecked){
        this.emailChecked = emailChecked;
    }
}
