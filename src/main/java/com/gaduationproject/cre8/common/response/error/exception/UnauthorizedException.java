package com.gaduationproject.cre8.common.response.error.exception;


import com.gaduationproject.cre8.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(ErrorCode code) {
        super(code.getMessage());
    }
}
