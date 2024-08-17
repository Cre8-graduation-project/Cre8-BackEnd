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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_field_tag_id")
    private WorkFieldTag workFieldTag;

    @Column(nullable = false)
    private String title;

    @Embedded
    private Payment payment;

    @Column(nullable = false,length = 2000)
    private String contents;

    @Column(nullable = false)
    private String contact;

    private String accessUrl;




    @Builder
    public BasicPostContent(final Member member,final String title, final WorkFieldTag workFieldTag, final PaymentMethod paymentMethod,
            final Integer paymentAmount,final String contents,final String contact,final String accessUrl) {

        this.member = member;
        this.title = title;
        this.workFieldTag = workFieldTag;
        this.contents = contents;
        this.contact = contact;
        this.accessUrl = accessUrl;

        this.payment = Payment.builder()
                .paymentAmount(paymentAmount)
                .paymentMethod(paymentMethod)
                .build();


    }

    public void changeExceptMember(final String title, final WorkFieldTag workFieldTag,final PaymentMethod paymentMethod,final Integer paymentAmount,
    final String contents,final String contact,final String accessUrl){

        this.title = title;
        this.workFieldTag = workFieldTag;
        payment.changePaymentMethodAndPaymentAmount(paymentMethod,paymentAmount);
        this.contact = contact;
        this.contents = contents;
        this.accessUrl = accessUrl;
    }
}
