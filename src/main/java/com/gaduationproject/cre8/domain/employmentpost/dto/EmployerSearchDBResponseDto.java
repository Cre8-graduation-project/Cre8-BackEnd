package com.gaduationproject.cre8.domain.employmentpost.dto;

import com.gaduationproject.cre8.domain.employmentpost.type.EnrollDurationType;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EmployerSearchDBResponseDto {

    private Long employerPostId;
    private String title;
    private String companyName;
    private EnrollDurationType enrollDurationType;
    private String accessUrl;
    private Optional<WorkFieldTag> workFieldTag;
    private List<EmployerPostWorkFieldChildTagSearchDBResponseDto> employerPostWorkFieldChildTagSearchDBResponseDtoList;

    @Builder
    public EmployerSearchDBResponseDto(final Long employerPostId, final String title, final String companyName,
            final WorkFieldTag workFieldTag,
            final EnrollDurationType enrollDurationType,
            final String accessUrl,
            final List<EmployerPostWorkFieldChildTagSearchDBResponseDto> employerPostWorkFieldChildTagSearchDBResponseDtoList) {

        this.employerPostId = employerPostId;
        this.title = title;
        this.companyName = companyName;
        this.enrollDurationType = enrollDurationType;
        this.employerPostWorkFieldChildTagSearchDBResponseDtoList = employerPostWorkFieldChildTagSearchDBResponseDtoList;
        this.accessUrl = accessUrl;
        this.workFieldTag = Optional.ofNullable(workFieldTag);
    }
}
