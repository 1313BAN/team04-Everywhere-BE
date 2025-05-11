package com.ssafy.enjoytrip.everywhere.common.constants;

import org.springframework.http.HttpStatus;

public enum SuccessCode {
	// user
	SUCCESS_DEFAULT(HttpStatus.OK, "요청을 성공적으로 처리했어요."),
	SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입에 성공했습니다.");

	private final HttpStatus code;
	private final String message;

	SuccessCode(HttpStatus code, String message) {
		this.code = code;
		this.message = message;
	}

	public HttpStatus code() {
		return code;
	}

	public String message() {
		return message;
	}
}