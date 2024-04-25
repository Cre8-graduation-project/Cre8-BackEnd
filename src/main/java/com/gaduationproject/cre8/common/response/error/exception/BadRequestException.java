package com.gaduationproject.cre8.common.response.error.exception;


import com.gaduationproject.cre8.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class BadRequestException extends RuntimeException {

    public BadRequestException(ErrorCode code) {
        super(code.getMessage());
    }
}
