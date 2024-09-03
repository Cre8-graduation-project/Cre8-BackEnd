package com.gaduationproject.cre8.domain.employmentpost.dto;

import com.gaduationproject.cre8.domain.employmentpost.type.EnrollDurationType;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EmployerPostKeyWordSearchDBResponseDto {

    private Long employerPostId;
    private String title;
    private String companyName;
    private EnrollDurationType enrollDurationType;
    private String accessUrl;
    private WorkFieldTag workFieldTag;

    @Builder
    public EmployerPostKeyWordSearchDBResponseDto(final Long employerPostId, final String title,
            final String companyName,
            final EnrollDurationType enrollDurationType, final String accessUrl, final WorkFieldTag workFieldTag) {
        this.employerPostId = employerPostId;
        this.title = title;
        this.companyName = companyName;
        this.enrollDurationType = enrollDurationType;
        this.accessUrl = accessUrl;
        this.workFieldTag = workFieldTag;
    }
}
