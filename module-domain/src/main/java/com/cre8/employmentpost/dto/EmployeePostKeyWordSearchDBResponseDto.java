package com.cre8.employmentpost.dto;


import com.cre8.member.type.Sex;
import com.cre8.workfieldtag.entity.WorkFieldTag;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EmployeePostKeyWordSearchDBResponseDto {

    private Long employeePostId;
    private String title;
    private String accessUrl;
    private WorkFieldTag workFieldTag;
    private String memberName;
    private Sex sex;
    private LocalDate birthDay;
   // private List<EmployeePostWorkFieldChildTag> employeePostWorkFieldChildTagList;

    @Builder
    public EmployeePostKeyWordSearchDBResponseDto(final Long employeePostId, final String title, final WorkFieldTag workFieldTag,
            final String memberName,
            final String accessUrl, final Sex sex,final LocalDate birthDay) {
        this.employeePostId = employeePostId;
        this.title = title;
        this.workFieldTag = workFieldTag;
        this.memberName = memberName;
        this.accessUrl = accessUrl;
        this.sex = sex;
        this.birthDay =birthDay;
     //   this.employeePostWorkFieldChildTagList = employeePostWorkFieldChildTagList;

    }

}
