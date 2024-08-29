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
public class TestEmployeePostSearchWithCountResponseDto {

    private Long totalCount;
    private List<TestEmployeePostSearchResponseDto> testEmployeePostSearchResponseDtoList = new ArrayList<>();
    private int totalPages;

    public static TestEmployeePostSearchWithCountResponseDto of(final Long totalCount,
            final List<TestEmployeePostSearchResponseDto> testEmployeePostSearchResponseDtoList,
            int totalPages){

        return new TestEmployeePostSearchWithCountResponseDto(totalCount,testEmployeePostSearchResponseDtoList,totalPages);
    }
}
