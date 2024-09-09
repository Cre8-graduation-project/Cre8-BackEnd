package com.gaduationproject.cre8.app.community.dto.response;

import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostSearchResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostSearchWithSliceResponseDto;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityPostSearchWithSliceResponseDto {

    private List<CommunityPostSearchResponseDto> communityPostSearchResponseDtoList = new ArrayList<>();
    private boolean hasNextPage;

    public static CommunityPostSearchWithSliceResponseDto of(
            final List<CommunityPostSearchResponseDto> communityPostSearchResponseDtoList,
            boolean hasNextPage){

        return new CommunityPostSearchWithSliceResponseDto(communityPostSearchResponseDtoList,hasNextPage);
    }

}
