package com.gaduationproject.cre8.common.response.error.exception;


import com.gaduationproject.cre8.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class DuplicateException extends RuntimeException {

    public DuplicateException(ErrorCode code) {
        super(code.getMessage());
    }
}
