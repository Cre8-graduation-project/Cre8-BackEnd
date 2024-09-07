package com.gaduationproject.cre8.domain.employmentpost.dto;

import java.util.Optional;
import lombok.Getter;

@Getter
public class EmployerPostWorkFieldChildTagSearchDBResponseDto {

    private Long employerPostWorkFieldChildTagId;
    private Optional<String> childTagName;




    public EmployerPostWorkFieldChildTagSearchDBResponseDto(final Long employerPostWorkFieldChildTagId,
            final String childTagName) {
        this.employerPostWorkFieldChildTagId = employerPostWorkFieldChildTagId;
        this.childTagName = Optional.ofNullable(childTagName);
    }

}
