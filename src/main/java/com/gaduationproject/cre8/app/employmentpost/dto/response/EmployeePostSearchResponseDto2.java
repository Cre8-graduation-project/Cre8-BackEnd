package com.gaduationproject.cre8.app.employmentpost.dto.response;

import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeeSearchDBResponseDto;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeePostSearchResponseDto2 {

    private Long employeePostId;
    private String title;
    private String memberName;
    private String sex;
    private int year;
    private List<String> tagNameList = new ArrayList<>();
    private String accessUrl;


    public static EmployeePostSearchResponseDto2 of(final EmployeeSearchDBResponseDto employeePost,final List<String> tagNameList){

        return new EmployeePostSearchResponseDto2(employeePost.getEmployeePostId(),
                employeePost.getTitle(),
                employeePost.getMemberName(),
                employeePost.getSex().getName(),
                employeePost.getBirthDay().getYear(),
                tagNameList,
                employeePost.getAccessUrl());
    }

}
