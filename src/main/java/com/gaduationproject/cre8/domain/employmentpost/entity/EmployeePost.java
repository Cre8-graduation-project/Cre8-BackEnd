package com.gaduationproject.cre8.domain.employmentpost.entity;

import com.gaduationproject.cre8.domain.baseentity.BaseEntity;
import com.gaduationproject.cre8.domain.employmentpost.type.PaymentMethod;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
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
public class EmployeePost extends BaseEntity {

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
    PaymentMethod paymentMethod,final Integer paymentAmount, final Integer careerYear,
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

        this.careerYear = careerYear;
    }

    public void changeAllExceptMemberAndId(final String title, final WorkFieldTag workFieldTag,final PaymentMethod paymentMethod,final Integer paymentAmount,
            final Integer careerYear,final String contents,final String contact,final String accessUrl){

        basicPostContent.changeExceptMember(title, workFieldTag,paymentMethod,paymentAmount,contents,contact,accessUrl);
        this.careerYear = careerYear;

    }

    public void setId(Long id){
        this.id = id;
    }
}
