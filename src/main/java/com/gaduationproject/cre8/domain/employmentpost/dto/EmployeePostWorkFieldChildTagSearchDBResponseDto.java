package com.gaduationproject.cre8.domain.employmentpost.dto;

import lombok.Getter;

@Getter
public class EmployeePostWorkFieldChildTagSearchDBResponseDto {

    private Long employeePostWorkFieldChildTagId;
    private String childTagName;




    public EmployeePostWorkFieldChildTagSearchDBResponseDto(final Long employeePostWorkFieldChildTagId,
            final String childTagName) {
        this.employeePostWorkFieldChildTagId = employeePostWorkFieldChildTagId;
        this.childTagName = childTagName;
    }
}
