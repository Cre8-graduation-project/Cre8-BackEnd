package com.cre8.response.error.exception;


import com.cre8.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class InterruptedException extends RuntimeException {

    public InterruptedException(ErrorCode code) {
        super(code.getMessage());
    }
}
