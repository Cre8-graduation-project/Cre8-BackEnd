package com.gaduationproject.cre8.employmentpost.dto.response;

import com.gaduationproject.cre8.employmentpost.domain.entity.EmployerPost;
import com.gaduationproject.cre8.employmentpost.service.EmployerPostSearchService;
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

    public static EmployerPostSearchResponseDto of(EmployerPost employerPost,List<String> tagNameList){
        return new EmployerPostSearchResponseDto(employerPost.getId(),employerPost.getBasicPostContent().getTitle(),
                employerPost.getCompanyName(), employerPost.getEnrollDurationType().getName(),tagNameList);
    }

}
