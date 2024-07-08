package com.gaduationproject.cre8.employmentpost.dto.response;

import com.gaduationproject.cre8.employmentpost.domain.entity.EmployerPost;
import com.gaduationproject.cre8.employmentpost.service.EmployerPostSearchService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployerPostSearchResponseDto {

    private Long employerPostId;

    public static EmployerPostSearchResponseDto of(EmployerPost employerPost){
        return new EmployerPostSearchResponseDto(employerPost.getId());
    }

}
