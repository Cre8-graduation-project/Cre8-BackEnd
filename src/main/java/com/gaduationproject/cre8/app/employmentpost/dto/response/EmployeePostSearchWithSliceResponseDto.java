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
public class EmployeePostSearchWithSliceResponseDto {

    private List<EmployeePostSearchResponseDto> employeePostSearchResponseDtoList = new ArrayList<>();
    private boolean hasNextPage;

    public static EmployeePostSearchWithSliceResponseDto of(
            final List<EmployeePostSearchResponseDto> employeePostSearchResponseDtoList,
            boolean hasNextPage){

        return new EmployeePostSearchWithSliceResponseDto(employeePostSearchResponseDtoList,hasNextPage);
    }

}
