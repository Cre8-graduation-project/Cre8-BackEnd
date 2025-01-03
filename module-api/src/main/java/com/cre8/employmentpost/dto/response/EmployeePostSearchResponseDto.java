package com.cre8.employmentpost.dto.response;


import com.cre8.employmentpost.dto.EmployeePostKeyWordSearchDBResponseDto;
import com.cre8.employmentpost.dto.EmployeeSearchDBResponseDto;
import com.cre8.employmentpost.entity.EmployeePost;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeePostSearchResponseDto {

    private Long employeePostId;
    private String title;
    private String memberName;
    private String sex;
    private int year;
    private List<String> tagNameList = new ArrayList<>();
    private String accessUrl;


    public static EmployeePostSearchResponseDto of(final EmployeePost employeePost,final List<String> tagNameList){

        return new EmployeePostSearchResponseDto(employeePost.getId(),employeePost.getBasicPostContent().getTitle(),
                employeePost.getBasicPostContent().getMember().getName(),
                employeePost.getBasicPostContent().getMember().getSex().getName(),
                employeePost.getBasicPostContent().getMember().getBirthDay().getYear(),
                tagNameList,
                employeePost.getBasicPostContent().getAccessUrl());
    }

    public static EmployeePostSearchResponseDto ofFaster(final EmployeeSearchDBResponseDto employeeSearchDBResponseDto,
                                                         final List<String> tagNameList){

        return new EmployeePostSearchResponseDto(employeeSearchDBResponseDto.getEmployeePostId(),employeeSearchDBResponseDto.getTitle(),
                employeeSearchDBResponseDto.getMemberName(),
                employeeSearchDBResponseDto.getSex().getName(),
                employeeSearchDBResponseDto.getBirthDay().getYear(),
                tagNameList,
                employeeSearchDBResponseDto.getAccessUrl());
    }

    public static EmployeePostSearchResponseDto ofSearch(final EmployeePostKeyWordSearchDBResponseDto employeePostKeyWordSearchDBResponseDto,final List<String> tagNameList){

        return new EmployeePostSearchResponseDto(employeePostKeyWordSearchDBResponseDto.getEmployeePostId(),employeePostKeyWordSearchDBResponseDto.getTitle(),
                employeePostKeyWordSearchDBResponseDto.getMemberName(),
                employeePostKeyWordSearchDBResponseDto.getSex().getName(),
                employeePostKeyWordSearchDBResponseDto.getBirthDay().getYear(),
                tagNameList,
                employeePostKeyWordSearchDBResponseDto.getAccessUrl());
    }

}
