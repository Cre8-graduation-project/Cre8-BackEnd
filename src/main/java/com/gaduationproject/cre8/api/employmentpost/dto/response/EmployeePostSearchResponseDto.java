package com.gaduationproject.cre8.api.employmentpost.dto.response;

import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
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


    public static EmployeePostSearchResponseDto of(final EmployeePost employeePost,final List<String> tagNameList){

        return new EmployeePostSearchResponseDto(employeePost.getId(),employeePost.getBasicPostContent().getTitle(),
                employeePost.getBasicPostContent().getMember().getName(),
                employeePost.getBasicPostContent().getMember().getSex().getName(),
                employeePost.getBasicPostContent().getMember().getBirthDay().getYear(),
                tagNameList);
    }

}
