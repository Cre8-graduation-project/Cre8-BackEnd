package com.gaduationproject.cre8.app.employmentpost.dto.response;

import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeeSearchResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeeSearchResponseDto2;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeeSearchResponseDto3;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TestEmployeePostSearchResponseDto {

    private Long employeePostId;
    private String title;
    private String memberName;
    private String sex;
    private int year;
    private List<String> tagNameList = new ArrayList<>();
    private String accessUrl;


    public static TestEmployeePostSearchResponseDto of(final EmployeeSearchResponseDto employeePost,final List<String> tagNameList){

        return new TestEmployeePostSearchResponseDto(employeePost.getEmployeePostId(),
                employeePost.getTitle(),
                employeePost.getMemberName(),
                employeePost.getSex().getName(),
                employeePost.getBirthDay().getYear(),
                tagNameList,
                employeePost.getAccessUrl());
    }

    public static TestEmployeePostSearchResponseDto of2(final EmployeeSearchResponseDto2 employeePost,final List<String> tagNameList){

        return new TestEmployeePostSearchResponseDto(employeePost.getEmployeePostId(),
                employeePost.getTitle(),
                employeePost.getMemberName(),
                employeePost.getSex().getName(),
                employeePost.getBirthDay().getYear(),
                tagNameList,
                employeePost.getAccessUrl());
    }

    public static TestEmployeePostSearchResponseDto of3(final EmployeeSearchResponseDto3 employeePost,final List<String> tagNameList){

        return new TestEmployeePostSearchResponseDto(employeePost.getEmployeePost().getId(),
                employeePost.getEmployeePost().getBasicPostContent().getTitle(),
                employeePost.getMemberName(),
                employeePost.getSex().getName(),
                employeePost.getBirthDay().getYear(),
                tagNameList,
                employeePost.getEmployeePost().getBasicPostContent().getAccessUrl());
    }
}
