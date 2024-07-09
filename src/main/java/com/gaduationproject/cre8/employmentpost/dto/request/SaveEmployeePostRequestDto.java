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
public class SaveEmployeePostRequestDto {

    private String title;
    private Long workFieldId;
    private List<Long> workFieldChildTagId = new ArrayList<>();
    private String paymentMethod;
    private Integer paymentAmount;
    private Integer careerYear;

    @Builder
    public SaveEmployeePostRequestDto(final String title, final Long workFieldId,
            final List<Long> workFieldChildTagId,
            final String paymentMethod, final Integer paymentAmount, final Integer careerYear) {

        this.title = title;
        this.workFieldId = workFieldId;
        this.workFieldChildTagId = workFieldChildTagId;
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
        this.careerYear = careerYear;
    }
}
