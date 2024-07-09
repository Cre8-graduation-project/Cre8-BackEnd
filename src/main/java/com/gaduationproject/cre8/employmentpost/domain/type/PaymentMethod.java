package com.gaduationproject.cre8.employmentpost.domain.type;

import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum PaymentMethod {

    PER_PIECE("작업물 건 당 지급"),PER_MINUTE("작업물 분 당 지급"),MONTH("월급"), ELSE("기타");

    private String name;

    PaymentMethod(String name){
        this.name = name;
    }


    public static PaymentMethod toPaymentMethodEnum(String paymentMethod){

        return Arrays.stream(PaymentMethod.values())
                .filter(PaymentMethod-> PaymentMethod.getName().equals(paymentMethod))
                .findAny()
                .orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_PAYMENT_METHOD));
    }
}
