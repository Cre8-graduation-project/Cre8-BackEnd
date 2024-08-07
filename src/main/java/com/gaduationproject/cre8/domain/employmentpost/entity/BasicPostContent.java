package com.gaduationproject.cre8.domain.employmentpost.entity;

import com.gaduationproject.cre8.domain.employmentpost.type.Payment;
import com.gaduationproject.cre8.domain.employmentpost.type.PaymentMethod;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BasicPostContent {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_field_tag_id")
    private WorkFieldTag workFieldTag;

    @Embedded
    private Payment payment;

    @Column(nullable = false,length = 2000)
    private String contents;




    @Builder
    public BasicPostContent(final Member member,final String title, final WorkFieldTag workFieldTag, final PaymentMethod paymentMethod,
            final Integer paymentAmount,final String contents) {

        this.member = member;
        this.title = title;
        this.workFieldTag = workFieldTag;
        this.contents = contents;

        payment = Payment.builder()
                .paymentAmount(paymentAmount)
                .paymentMethod(paymentMethod)
                .build();


    }

    public void changeExceptMember(final String title, final WorkFieldTag workFieldTag,final PaymentMethod paymentMethod,final Integer paymentAmount,
    final String contents){

        this.title = title;
        this.workFieldTag = workFieldTag;
        payment.changePaymentMethodAndPaymentAmount(paymentMethod,paymentAmount);
        this.contents = contents;
    }
}
