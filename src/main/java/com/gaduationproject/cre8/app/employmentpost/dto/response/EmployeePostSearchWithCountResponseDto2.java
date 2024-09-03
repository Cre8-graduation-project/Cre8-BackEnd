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
public class EmployeePostSearchWithCountResponseDto2 {

    private Long totalCount;
    private List<EmployeePostSearchResponseDto2> employeePostSearchResponseDto2List = new ArrayList<>();
    private int totalPages;

    public static EmployeePostSearchWithCountResponseDto2 of(final Long totalCount,
            final List<EmployeePostSearchResponseDto2> employeePostSearchResponseDto2List,
            int totalPages){

        return new EmployeePostSearchWithCountResponseDto2(totalCount,
                employeePostSearchResponseDto2List,totalPages);
    }
}
