package com.gaduationproject.cre8.domain.employmentpost.dto;

import lombok.Getter;

@Getter
public class EmployerPostWorkFieldChildTagSearchDBResponseDto {

    private Long employerPostWorkFieldChildTagId;
    private String childTagName;




    public EmployerPostWorkFieldChildTagSearchDBResponseDto(final Long employerPostWorkFieldChildTagId,
            final String childTagName) {
        this.employerPostWorkFieldChildTagId = employerPostWorkFieldChildTagId;
        this.childTagName = childTagName;
    }

}
