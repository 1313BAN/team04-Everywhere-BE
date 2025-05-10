package com.ssafy.enjoytrip.everywhere.common.exception;

import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;

public class InvalidTokenException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidTokenException(ErrorCode errorCode) {
        super(errorCode.message()); // 부모 클래스 메시지 설정
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}