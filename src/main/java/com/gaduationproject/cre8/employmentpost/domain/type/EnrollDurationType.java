package com.gaduationproject.cre8.employmentpost.domain.type;

import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum EnrollDurationType {
    ALWAYS("상시 채용"),DEAD_LINE("마감일 지정"),AT_COMPLETED("채용시 마감");

    private String name;

    EnrollDurationType(String name){
        this.name = name;
    }

    public static EnrollDurationType toEnrollDurationTypeEnum(String enrollDurationType){

        return Arrays.stream(EnrollDurationType.values())
                .filter(EnrollDurationType-> EnrollDurationType.getName().equals(enrollDurationType))
                .findAny()
                .orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_ENROLL_DURATION_TYPE));
    }
}
