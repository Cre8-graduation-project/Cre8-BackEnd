package com.cre8.employmentpost.dto;


import com.cre8.member.type.Sex;
import com.cre8.workfieldtag.entity.WorkFieldTag;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EmployeeSearchDBResponseDto {

    private Long employeePostId;
    private String title;
    private String accessUrl;
    private Optional<WorkFieldTag> workFieldTag;
    private String memberName;
    private Sex sex;
    private LocalDate birthDay;
    private List<EmployeePostWorkFieldChildTagSearchDBResponseDto> employeePostWorkFieldChildTagSearchDBResponseDtoList;




    @Builder
    public EmployeeSearchDBResponseDto(final Long employeePostId, final String title, final WorkFieldTag workFieldTag,
            final String memberName,
            final String accessUrl, final Sex sex,final LocalDate birthDay,final List<EmployeePostWorkFieldChildTagSearchDBResponseDto> employeePostWorkFieldChildTagSearchDBResponseDtoList


    ) {
        this.employeePostId = employeePostId;
        this.title = title;
        this.workFieldTag = Optional.ofNullable(workFieldTag);
        this.memberName = memberName;
        this.accessUrl = accessUrl;
        this.sex = sex;
        this.birthDay =birthDay;
        this.employeePostWorkFieldChildTagSearchDBResponseDtoList = employeePostWorkFieldChildTagSearchDBResponseDtoList;

    }

}
