package com.gaduationproject.cre8.domain.employmentpost.dto;

import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeePostWorkFieldChildTagSearchResponseDto;
import com.gaduationproject.cre8.domain.member.type.Sex;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class EmployeeSearchResponseDto {

    private Long employeePostId;
    private String title;
    private String accessUrl;
    private WorkFieldTag workFieldTag;
    private String memberName;
    private Sex sex;
    private LocalDate birthDay;
    private List<EmployeePostWorkFieldChildTagSearchResponseDto> employeePostWorkFieldChildTagSearchResponseDtoList;
    private String memberWriting;
    private String postWriting;


    public EmployeeSearchResponseDto(Long employeePostId, String title, WorkFieldTag workFieldTag,
            String memberName,
            String accessUrl, Sex sex,LocalDate birthDay,List<EmployeePostWorkFieldChildTagSearchResponseDto> employeePostWorkFieldChildTagSearchResponseDtoList,
            String memberWriting,String postWriting

    ) {
        this.employeePostId = employeePostId;
        this.title = title;
        this.workFieldTag = workFieldTag;
        this.memberName = memberName;
        this.accessUrl = accessUrl;
        this.sex = sex;
        this.birthDay =birthDay;
        this.employeePostWorkFieldChildTagSearchResponseDtoList =employeePostWorkFieldChildTagSearchResponseDtoList;
        this.memberWriting = memberWriting;
        this.postWriting = postWriting;
    }

}
