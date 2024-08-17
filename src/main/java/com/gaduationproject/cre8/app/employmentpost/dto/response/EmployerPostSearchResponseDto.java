package com.gaduationproject.cre8.app.employmentpost.dto.response;

import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployerPostSearchResponseDto {

    private Long employerPostId;
    private String title;
    private String companyName;
    private String  enrollDurationType;
    private List<String> tagNameList = new ArrayList<>();
    private String accessUrl;

    public static EmployerPostSearchResponseDto of(final EmployerPost employerPost,final List<String> tagNameList){

        return new EmployerPostSearchResponseDto(employerPost.getId(),
                employerPost.getBasicPostContent().getTitle(),
                employerPost.getCompanyName(),
                employerPost.getEnrollDurationType().getName(),
                tagNameList,
                employerPost.getBasicPostContent().getAccessUrl());
    }

}
