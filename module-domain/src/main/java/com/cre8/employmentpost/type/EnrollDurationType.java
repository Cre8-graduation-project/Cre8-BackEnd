package com.cre8.employmentpost.type;


import com.cre8.response.error.ErrorCode;
import com.cre8.response.error.exception.NotFoundException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum EnrollDurationType {
    ALWAYS("상시 채용"),DEAD_LINE("마감일 지정"),AT_COMPLETED("채용 시 마감");

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
