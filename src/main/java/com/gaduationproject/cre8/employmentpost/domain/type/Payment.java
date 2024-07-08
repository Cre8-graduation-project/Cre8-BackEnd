package com.gaduationproject.cre8.employmentpost.domain.type;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Payment {

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private Integer paymentAmount;

    @Builder
    public Payment(PaymentMethod paymentMethod, Integer paymentAmount){
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
    }

    public void changePaymentMethodAndPaymentAmount(PaymentMethod paymentMethod,Integer paymentAmount){
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
    }

}
