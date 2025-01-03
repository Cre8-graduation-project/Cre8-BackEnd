package com.cre8.response.error.exception;


import com.cre8.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class NoSuchElementException extends RuntimeException {

    public NoSuchElementException(ErrorCode code) {
        super(code.getMessage());
    }
}
