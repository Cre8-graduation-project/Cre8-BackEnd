package com.gaduationproject.cre8.app.employmentpost.dto.response;

import com.gaduationproject.cre8.domain.employmentpost.dto.EmployerSearchDBResponseDto;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployerPostSearchResponseDto2 {

    private Long employerPostId;
    private String title;
    private String companyName;
    private String  enrollDurationType;
    private List<String> tagNameList = new ArrayList<>();
    private String accessUrl;


    public static EmployerPostSearchResponseDto2 of(final EmployerSearchDBResponseDto employerSearchDBResponseDto
                                                   ,final List<String> tagNameList){

        return new EmployerPostSearchResponseDto2(employerSearchDBResponseDto.getEmployerPostId(),
                employerSearchDBResponseDto.getTitle(),
                employerSearchDBResponseDto.getCompanyName(),
                employerSearchDBResponseDto.getEnrollDurationType().getName(),
                tagNameList,
                employerSearchDBResponseDto.getAccessUrl());

    }
}
