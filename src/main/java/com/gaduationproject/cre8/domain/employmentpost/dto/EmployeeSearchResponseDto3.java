package com.gaduationproject.cre8.domain.employmentpost.dto;

import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.type.PaymentMethod;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.type.Sex;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class EmployeeSearchResponseDto3 {

    private Long employeePostId;
    private String accessUrl;
    private String contact;
    private Member member;
    private Integer paymentAmount;
    private PaymentMethod paymentMethod;
    private String title;
    private WorkFieldTag workFieldTag;
    private Integer careerYear;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String memberName;
    private Sex sex;
    private LocalDate birthDay;


    public EmployeeSearchResponseDto3(Long employeePostId, String accessUrl, String contact,
            Member member, Integer paymentAmount, PaymentMethod paymentMethod, String title,
            WorkFieldTag workFieldTag, Integer careerYear, LocalDateTime createdAt,
            LocalDateTime modifiedAt, String memberName, Sex sex, LocalDate birthDay) {
        this.employeePostId = employeePostId;
        this.accessUrl = accessUrl;
        this.contact = contact;
        this.member = member;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.title = title;
        this.workFieldTag = workFieldTag;
        this.careerYear = careerYear;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.memberName = memberName;
        this.sex = sex;
        this.birthDay = birthDay;
    }
}
