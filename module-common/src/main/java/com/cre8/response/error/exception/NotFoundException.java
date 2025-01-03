package com.cre8.response.error.exception;


import com.cre8.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class NotFoundException extends RuntimeException {

    public NotFoundException(ErrorCode code) {
        super(code.getMessage());
    }
}
