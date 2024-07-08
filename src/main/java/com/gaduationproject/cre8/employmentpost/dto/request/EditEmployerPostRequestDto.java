package com.gaduationproject.cre8.employmentpost.dto.request;

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

    private Long workFieldId;

    private String title;

    private List<Long> workFieldChildTagId = new ArrayList<>();

    private String paymentMethod;

    private Integer payment;

    private Integer numberOfEmployee;

    private String enrollDurationType;

    private LocalDate deadLine;

    private Integer minCareerYear;


    @Builder
    public EditEmployerPostRequestDto(Long employerPostId, Long workFieldId,String title, List<Long> workFieldChildTagId,
            String paymentMethod, Integer payment, Integer numberOfEmployee,
            String enrollDurationType,LocalDate deadLine, Integer minCareerYear) {
        this.employerPostId = employerPostId;
        this.workFieldId = workFieldId;
        this.title = title;
        this.workFieldChildTagId = workFieldChildTagId;
        this.paymentMethod = paymentMethod;
        this.payment = payment;
        this.numberOfEmployee = numberOfEmployee;
        this.enrollDurationType = enrollDurationType;
        this.deadLine = deadLine;
        this.minCareerYear = minCareerYear;
    }

}
