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
    NOT_VALIDATE_EMAIL_AUTH_NUMBER("유효한 인증번호가 아닙니다"),
    LOGIN_ID_NOT_MATCH("아이디를 다시 확인해주세요"),
    PASSWORD_NOT_MATCH("비밀번호를 다시 확인해주세요"),
    ACCESS_TOKEN_NOT_MATCH("Access Token 을 다시 확인해주세요"),
    REFRESH_TOKEN_NOT_MATCH("Refresh Token을  을 다시 확인해주세요"),


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
    EMAIL_AUTH_EXPIRED("이메일 인증을 다시 시도해주세요"),


    /**
     * 405 Method Not Allowed


     /**
     * 409 Conflict
     */

    DUPLICATE_EMAIL("이메일이 중복되었습니다"),
    DUPLICATE_NICKNAME("닉네임이 중복되었습니다"),
    DUPLICATE_LOGIN_ID("이미 아이디가 존재합니다"),



    /**
     * 500 Internal Server Error
     */


    /**
     * 500 Internal Server Error
     */

    MAIL_SERVER_ERROR("이메일 서버의 오류로 나중에 시도하세요");

    private final String message;
}