package com.gaduationproject.cre8.app.employmentpost.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployerPostSearchWithCountResponseDto2 {

    private Long totalCount;
    private List<EmployerPostSearchResponseDto2> employerPostSearchResponseDtoList = new ArrayList<>();
    private int totalPages;

    public static EmployerPostSearchWithCountResponseDto2 of(final Long totalCount,
            final List<EmployerPostSearchResponseDto2> employerPostSearchResponseDtoList,
            int totalPages){

        return new EmployerPostSearchWithCountResponseDto2(totalCount,employerPostSearchResponseDtoList,totalPages);
    }

}
