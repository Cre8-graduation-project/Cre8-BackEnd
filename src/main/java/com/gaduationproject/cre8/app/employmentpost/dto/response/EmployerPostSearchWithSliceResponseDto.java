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
public class EmployerPostSearchWithSliceResponseDto {


    private List<EmployerPostSearchResponseDto> employerPostSearchResponseDtoList = new ArrayList<>();
    private boolean hasNextPage;

    public static EmployerPostSearchWithSliceResponseDto of(
            final List<EmployerPostSearchResponseDto> employerPostSearchResponseDtoList,
            boolean hasNextPage){

        return new EmployerPostSearchWithSliceResponseDto(employerPostSearchResponseDtoList,hasNextPage);
    }

}
