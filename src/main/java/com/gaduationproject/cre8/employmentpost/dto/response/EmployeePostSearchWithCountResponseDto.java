package com.gaduationproject.cre8.employmentpost.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeePostSearchWithCountResponseDto {

    private Long totalCount;
    private List<EmployeePostSearchResponseDto> employeePostSearchResponseDtoList = new ArrayList<>();
    private int totalPages;

    public static EmployeePostSearchWithCountResponseDto of(final Long totalCount,
            final List<EmployeePostSearchResponseDto> employeePostSearchResponseDtoList,
            int totalPages){

        return new EmployeePostSearchWithCountResponseDto(totalCount,employeePostSearchResponseDtoList,totalPages);
    }

}
