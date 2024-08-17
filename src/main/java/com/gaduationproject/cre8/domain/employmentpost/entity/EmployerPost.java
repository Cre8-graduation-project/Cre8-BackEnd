package com.gaduationproject.cre8.domain.employmentpost.entity;

import com.gaduationproject.cre8.domain.baseentity.BaseEntity;
import com.gaduationproject.cre8.domain.employmentpost.type.EnrollDurationType;
import com.gaduationproject.cre8.domain.employmentpost.type.PaymentMethod;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import jakarta.persistence.Column;
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

    @Column(nullable = false)
    private String companyName;

    private Integer numberOfEmployee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollDurationType enrollDurationType;

    private Integer hopeCareerYear;

    private LocalDate deadLine;

    @OneToMany(mappedBy = "employerPost")
    List<EmployerPostWorkFieldChildTag> employerPostWorkFieldChildTagList = new ArrayList<>();



    @Builder
    public EmployerPost(final Member member,final String title, final WorkFieldTag workFieldTag, final PaymentMethod paymentMethod, final Integer paymentAmount,final String companyName, final Integer numberOfEmployee,
             final EnrollDurationType enrollDurationType, final LocalDate deadLine,final Integer hopeCareerYear,
            final String contents,final String contact,final String accessUrl) {

        this.basicPostContent = BasicPostContent.builder()
                .member(member)
                .title(title)
                .workFieldTag(workFieldTag)
                .paymentMethod(paymentMethod)
                .paymentAmount(paymentAmount)
                .contents(contents)
                .contact(contact)
                .accessUrl(accessUrl)
                .build();

        this.companyName = companyName;
        this.numberOfEmployee = numberOfEmployee;
        this.enrollDurationType = enrollDurationType;
        this.hopeCareerYear = hopeCareerYear;
        this.deadLine = deadLine;
    }

    public void changeAllExceptMemberAndId(final String title, final WorkFieldTag workFieldTag,final PaymentMethod paymentMethod,final Integer paymentAmount,final String companyName, final Integer numberOfEmployee,
            final EnrollDurationType enrollDurationType,final LocalDate deadLine,final Integer hopeCareerYear,
            final String contents,final String contact,final String accessUrl){

        basicPostContent.changeExceptMember(title, workFieldTag,paymentMethod,paymentAmount,contents,contact,accessUrl);
        this.companyName = companyName;
        this.numberOfEmployee = numberOfEmployee;
        this.enrollDurationType = enrollDurationType;
        this.hopeCareerYear = hopeCareerYear;
        this.deadLine = deadLine;

    }
}
