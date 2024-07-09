package com.gaduationproject.cre8.employmentpost.domain.entity;

import com.gaduationproject.cre8.employmentpost.domain.type.PaymentMethod;
import com.gaduationproject.cre8.member.entity.Member;
import com.gaduationproject.cre8.workfieldtag.entity.WorkFieldTag;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EmployeePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_post_id")
    private Long id;
    @Embedded
    private BasicPostContent basicPostContent;
    private Integer careerYear;

    @OneToMany(mappedBy = "employeePost")
    List<EmployeePostWorkFieldChildTag> employeePostWorkFieldChildTagList = new ArrayList<>();

    @Builder
    public EmployeePost(final Member member,final String title,final WorkFieldTag workFieldTag,final
            PaymentMethod paymentMethod,final Integer paymentAmount, Integer careerYear) {

        this.basicPostContent = BasicPostContent.builder()
                .member(member)
                .title(title)
                .workFieldTag(workFieldTag)
                .paymentMethod(paymentMethod)
                .paymentAmount(paymentAmount)
                .build();
        this.careerYear = careerYear;
    }
}
