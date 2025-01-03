package com.cre8.response.error.exception;


import com.cre8.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class InternalServerErrorException extends RuntimeException {

    public InternalServerErrorException(ErrorCode code) {
        super(code.getMessage());
    }
}
