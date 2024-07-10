package com.gaduationproject.cre8.employmentpost.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditEmployerPostRequestDto {

    private Long employerPostId;
    private String title;
    private Long workFieldId;
    private List<Long> workFieldChildTagId = new ArrayList<>();
    private String paymentMethod;
    private Integer paymentAmount;
    private String companyName;
    private Integer numberOfEmployee;
    private String enrollDurationType;
    private LocalDate deadLine;
    private Integer hopeCareerYear;
    private String contents;


    @Builder
    public EditEmployerPostRequestDto(final Long employerPostId, final Long workFieldId,final String title, final List<Long> workFieldChildTagId,
            final String paymentMethod, final Integer paymentAmount,final String companyName, final Integer numberOfEmployee,
            final String enrollDurationType,final LocalDate deadLine, final Integer hopeCareerYear,final String contents) {
        this.employerPostId = employerPostId;
        this.workFieldId = workFieldId;
        this.title = title;
        this.workFieldChildTagId = workFieldChildTagId;
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
        this.companyName = companyName;
        this.numberOfEmployee = numberOfEmployee;
        this.enrollDurationType = enrollDurationType;
        this.deadLine = deadLine;
        this.hopeCareerYear = hopeCareerYear;
        this.contents = contents;
    }

}
