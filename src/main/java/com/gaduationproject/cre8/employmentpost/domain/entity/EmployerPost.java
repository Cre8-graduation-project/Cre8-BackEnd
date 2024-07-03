package com.gaduationproject.cre8.employmentpost.domain.entity;

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
public class EmployerPost  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employer_post_id")
    private Long id;

    @Embedded
    private BasicPostContent basicPostContent;

    private Integer numberOfEmployee;

    @Enumerated(EnumType.STRING)
    private EnrollDurationType enrollDurationType;

    private Integer minCareerYear;

    private LocalDate deadLine;

    @OneToMany(mappedBy = "employerPost")
    List<EmployerPostWorkFieldChildTag> employerPostWorkFieldChildTagList = new ArrayList<>();

    @Builder
    public EmployerPost(Member member, WorkFieldTag workFieldTag, PaymentMethod paymentMethod, Integer payment, Integer numberOfEmployee,
             EnrollDurationType enrollDurationType, LocalDate deadLine,Integer minCareerYear) {

        this.basicPostContent = BasicPostContent.builder()
                .member(member)
                .workFieldTag(workFieldTag)
                .paymentMethod(paymentMethod)
                .payment(payment)
                .build();
        this.numberOfEmployee = numberOfEmployee;
        this.enrollDurationType = enrollDurationType;
        this.minCareerYear = minCareerYear;
        this.deadLine = deadLine;
    }

    public void changeAllExceptMemberAndId(WorkFieldTag workFieldTag,PaymentMethod paymentMethod,Integer payment, Integer numberOfEmployee,
            EnrollDurationType enrollDurationType,LocalDate deadLine,Integer minCareerYear){

        basicPostContent.changeExceptMember(workFieldTag,paymentMethod,payment);
        this.numberOfEmployee = numberOfEmployee;
        this.enrollDurationType = enrollDurationType;
        this.minCareerYear = minCareerYear;
        this.deadLine = deadLine;

    }
}
