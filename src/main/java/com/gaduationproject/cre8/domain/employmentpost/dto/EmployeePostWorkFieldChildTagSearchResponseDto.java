package com.gaduationproject.cre8.domain.employmentpost.dto;

import lombok.Getter;

@Getter
public class EmployeePostWorkFieldChildTagSearchResponseDto {

    private Long employeePostWorkFieldChildTagId;
    private String childTagName;



    public EmployeePostWorkFieldChildTagSearchResponseDto(Long employeePostWorkFieldChildTagId,
            String childTagName) {
        this.employeePostWorkFieldChildTagId = employeePostWorkFieldChildTagId;
        this.childTagName = childTagName;
    }
}
