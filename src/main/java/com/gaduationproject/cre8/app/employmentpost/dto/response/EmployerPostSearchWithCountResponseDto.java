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
public class EmployerPostSearchWithCountResponseDto {

    private Long totalCount;
    private List<EmployerPostSearchResponseDto> employerPostSearchResponseDtoList = new ArrayList<>();
    private int totalPages;

    public static EmployerPostSearchWithCountResponseDto of(final Long totalCount,
                                                            final List<EmployerPostSearchResponseDto> employerPostSearchResponseDtoList,
                                                            int totalPages){

        return new EmployerPostSearchWithCountResponseDto(totalCount,employerPostSearchResponseDtoList,totalPages);
    }

}
