package com.ssafy.enjoytrip.everywhere.common.exception;

import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final ErrorCode errorCode;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.message());
        this.errorCode = errorCode;
    }

}