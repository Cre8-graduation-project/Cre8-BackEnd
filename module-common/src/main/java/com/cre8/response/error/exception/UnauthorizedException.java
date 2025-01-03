package com.cre8.response.error.exception;


import com.cre8.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(ErrorCode code) {
        super(code.getMessage());
    }
}
