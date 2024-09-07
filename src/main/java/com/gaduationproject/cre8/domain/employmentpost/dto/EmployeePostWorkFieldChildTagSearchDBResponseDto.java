package com.gaduationproject.cre8.domain.employmentpost.dto;

import java.util.Optional;
import lombok.Getter;

@Getter
public class EmployeePostWorkFieldChildTagSearchDBResponseDto {

    private Long employeePostWorkFieldChildTagId;
    private Optional<String> childTagName;




    public EmployeePostWorkFieldChildTagSearchDBResponseDto(final Long employeePostWorkFieldChildTagId,
            final String childTagName) {
        this.employeePostWorkFieldChildTagId = employeePostWorkFieldChildTagId;
        this.childTagName = Optional.ofNullable(childTagName);
    }
}
