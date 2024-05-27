package com.gaduationproject.cre8.common.response.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    /**
     * 400 Bad Request
     */
    BAD_REQUEST("잘못된 요청입니다."),

    /**
     * 401 Unauthorized
     */

    /**
     * 403 Forbidden
     */


    /**
     * 404 Not Found
     */
    MESSAGE_NOT_FOUND("자원을 찾지 못함"),
    /**
     * 405 Method Not Allowed


     /**
     * 409 Conflict
     */

    DUPLICATE_EMAIL("이메일이 중복되었습니다"),
    DUPLICATE_NICKNAME("닉네임이 중복되었습니다");



    /**
     * 500 Internal Server Error
     */


    /**
     * 500 Internal Server Error
     */

    private final String message;
}