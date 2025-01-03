package com.cre8.response.error.exception;


import com.cre8.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class BadRequestException extends RuntimeException {

    public BadRequestException(ErrorCode code) {
        super(code.getMessage());
    }
}
