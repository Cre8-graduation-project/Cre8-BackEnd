package com.gaduationproject.cre8.employmentpost.dto.request;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditEmployeePostRequestDto {

    private Long employeePostId;
    private String title;
    private Long workFieldId;
    private List<Long> workFieldChildTagId = new ArrayList<>();
    private String paymentMethod;
    private Integer paymentAmount;
    private Integer careerYear;
    private String contents;

    @Builder
    public EditEmployeePostRequestDto(final Long employeePostId,final String title, final Long workFieldId,
            final List<Long> workFieldChildTagId,
            final String paymentMethod, final Integer paymentAmount, final Integer careerYear,final String contents) {

        this.employeePostId = employeePostId;
        this.title = title;
        this.workFieldId = workFieldId;
        this.workFieldChildTagId = workFieldChildTagId;
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
        this.careerYear = careerYear;
        this.contents = contents;
    }

}
