package com.gaduationproject.cre8.domain.employmentpost.dto;

import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.member.type.Sex;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class EmployeeSearchResponseDto3 {

    private EmployeePost employeePost;
    private String memberName;
    private Sex sex;
    private LocalDate birthDay;

    public EmployeeSearchResponseDto3(EmployeePost employeePost,
            String memberName,Sex sex,LocalDate birthDay
    ) {
        this.employeePost = employeePost;
        this.memberName = memberName;
        this.sex = sex;
        this.birthDay =birthDay;
    }



}
