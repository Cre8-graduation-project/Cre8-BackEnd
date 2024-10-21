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
    NOT_CORRECT_PARENT_TAG("하위 태그의 부모 코드가 일치하지 않습니다"),
    CANT_ACCESS_PORTFOLIO("자신의 포트폴리오만 수정,삭제할 수 있습니다"),
    CANT_ACCESS_EMPLOYER_POST("자신의 구인글만 수정, 삭제할 수 있습니다"),
    CANT_ACCESS_EMPLOYEE_POST("자신의 구직글만 수정, 삭제할 수 있습니다"),
    CANT_SET_DEADLINE_WITH_NO_ENUM_DEADLINE("데드라인을 선택해야 날짜를 입력할 수 있습니다"),
    INSERT_DEADLINE_ON_ENUM_DEADLINE("데드라인을 선택하면 날짜를 입력하셔야 합니다"),
    SUB_URL_NOT_MATCH("유효한 구독 URL 경로가 아닙니다"),
    SUB_URL_CANT_ACCESS("접근할 수 있는 구독 아이디가 아닙니다"),
    PUB_URL_CANT_ACCESS("해당 채팅방 참가하지 않았기에 pub 를 할 수 없습니다"),
    CANT_ACCESS_CHAT_ROOM("자신의 채팅방의 채팅만 조회가능합니다"),
    NOT_ALLOWABLE_EXTENSION("지원되지 않는 확장자입니다"),
    CANT_ACCESS_COMMUNITY_POSt("자신의 커뮤니티글만 수정, 삭제할 수 있습니다"),
    CANT_MAKE_RE_RE_REPLY("대대댓글은 만들 수 없습니다"),
    CANT_ACCESS_COMMUNITY_POST("자신의 게시글만 수정,삭제 할 수 있습니다"),
    CANT_ACCESS_REPLY("자신의 댓글만 수정,삭제 할 수 있습니다"),
    CANT_REQUEST_TMP_PW("사용자의 정보를 정확히 입력해주세요"),



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
    CANT_FIND_MEMBER("사용자가 존재하지 않습니다"),
    CANT_FIND_WORK_FILED_TAG("작업 분야 태그가 존재하지 않습니다"),
    CANT_FIND_WORK_FIELD_SUB_CATEGORY("작업 분야 하위 카테고리가 존재하지 않습니다"),
    CANT_FIND_WORK_FIELD_CHILD_TAG("하위 태그가 존재하지 않습니다"),
    CANT_FIND_PORTFOLIO("포트폴리오를 찾을 수 없습니다"),
    CANT_FIND_EMPLOYER_POST("구인글을 찾을 수 없습니다"),
    CANT_FIND_EMPLOYEE_POST("구직글을 찾을 수 없습니다"),
    CANT_FIND_PAYMENT_METHOD("해당 급여 지급 방식을 다시 확인해주세요"),
    CANT_FIND_ENROLL_DURATION_TYPE("해당 채용 방식을 다시 확인해주세요"),
    CANT_FIND_CHATTING_ROOM("채팅방을 찾을 수 없습니다"),
    CANT_FIND_PORTFOLIO_IMAGE_ID("해당 포트폴리오 이미지 아이디 기반으로 포트폴리오 이미지를 찾을 수 없습니다"),
    CANT_FIND_COMMUNITY_BOARD("해당 게시판을 찾을 수 없습니다"),
    CANT_FIND_COMMUNITY_POST("해당 게시글을 찾을 수 없습니다"),
    CANT_FIND_REPLY("댓글을 찾을 수 없습니다"),


    /**
     * 405 Method Not Allowed


     /**
     * 409 Conflict
     */

    DUPLICATE_EMAIL("이메일이 중복되었습니다"),
    DUPLICATE_NICKNAME("닉네임이 중복되었습니다"),
    DUPLICATE_LOGIN_ID("이미 아이디가 존재합니다"),
    DUPLICATE_WORK_FIELD_NAME("작업 분야 이름이 이미 존재합니다"),
    DUPLICATE_SUB_CATEGORY_NAME("이미 동일 분야에 같은 이름의 하위 카테고리가 존재합니다"),
    DUPLICATE_WORK_FIELD_CHILD_NAME("이미 동일 분야에 같은 작업 분야 이름이 이미 존재합니다"),




    /**
     * 500 Internal Server Error
     */
    NOT_APPLY_RECENT_CHAT_FILTER("최근 메시지 의 빈값을 거르기 위한 filter가 동작하지 않습니다"),
    /**
     * 500 Internal Server Error
     */

    MAIL_SERVER_ERROR("이메일 서버의 오류로 나중에 시도하세요");

    private final String message;
}