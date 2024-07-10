package com.gaduationproject.cre8.employmentpost.domain.entity;

import com.gaduationproject.cre8.common.baseentity.BaseEntity;
import com.gaduationproject.cre8.employmentpost.domain.type.EnrollDurationType;
import com.gaduationproject.cre8.employmentpost.domain.type.PaymentMethod;
import com.gaduationproject.cre8.member.entity.Member;
import com.gaduationproject.cre8.workfieldtag.entity.WorkFieldTag;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EmployerPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employer_post_id")
    private Long id;

    @Embedded
    private BasicPostContent basicPostContent;

    private String companyName;

    private Integer numberOfEmployee;

    @Enumerated(EnumType.STRING)
    private EnrollDurationType enrollDurationType;

    private Integer hopeCareerYear;

    private LocalDate deadLine;

    @OneToMany(mappedBy = "employerPost")
    List<EmployerPostWorkFieldChildTag> employerPostWorkFieldChildTagList = new ArrayList<>();



    @Builder
    public EmployerPost(Member member,String title, WorkFieldTag workFieldTag, PaymentMethod paymentMethod, Integer paymentAmount,String companyName, Integer numberOfEmployee,
             EnrollDurationType enrollDurationType, LocalDate deadLine,Integer hopeCareerYear,String contents) {

        this.basicPostContent = BasicPostContent.builder()
                .member(member)
                .title(title)
                .workFieldTag(workFieldTag)
                .paymentMethod(paymentMethod)
                .paymentAmount(paymentAmount)
                .contents(contents)
                .build();
        this.companyName = companyName;
        this.numberOfEmployee = numberOfEmployee;
        this.enrollDurationType = enrollDurationType;
        this.hopeCareerYear = hopeCareerYear;
        this.deadLine = deadLine;
    }

    public void changeAllExceptMemberAndId(String title, WorkFieldTag workFieldTag,PaymentMethod paymentMethod,Integer paymentAmount,String companyName, Integer numberOfEmployee,
            EnrollDurationType enrollDurationType,LocalDate deadLine,Integer hopeCareerYear,String contents){

        basicPostContent.changeExceptMember(title, workFieldTag,paymentMethod,paymentAmount,contents);
        this.companyName = companyName;
        this.numberOfEmployee = numberOfEmployee;
        this.enrollDurationType = enrollDurationType;
        this.hopeCareerYear = hopeCareerYear;
        this.deadLine = deadLine;

    }
}
