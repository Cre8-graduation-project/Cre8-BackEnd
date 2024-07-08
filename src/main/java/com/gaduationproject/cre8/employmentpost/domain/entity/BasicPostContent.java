package com.gaduationproject.cre8.employmentpost.domain.entity;

import com.gaduationproject.cre8.employmentpost.domain.type.PaymentMethod;
import com.gaduationproject.cre8.member.entity.Member;
import com.gaduationproject.cre8.workfieldtag.entity.WorkFieldChildTag;
import com.gaduationproject.cre8.workfieldtag.entity.WorkFieldTag;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BasicPostContent {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_field_tag_id")
    private WorkFieldTag workFieldTag;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private Integer payment;




    @Builder
    public BasicPostContent(Member member,String title, WorkFieldTag workFieldTag, PaymentMethod paymentMethod,
            Integer payment) {
        this.member = member;
        this.title = title;
        this.workFieldTag = workFieldTag;
        this.paymentMethod = paymentMethod;
        this.payment = payment;
    }

    public void changeExceptMember(String title, WorkFieldTag workFieldTag,PaymentMethod paymentMethod,int payment){
        this.title = title;
        this.workFieldTag = workFieldTag;
        this.paymentMethod = paymentMethod;
        this.payment = payment;
    }
}
