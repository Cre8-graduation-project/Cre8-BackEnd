package com.cre8.response.error.exception;


import com.cre8.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class DuplicateException extends RuntimeException {

    public DuplicateException(ErrorCode code) {
        super(code.getMessage());
    }
}
