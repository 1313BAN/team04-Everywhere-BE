package com.ssafy.enjoytrip.everywhere.common.constants;

import org.springframework.http.HttpStatus;

public enum SuccessCode {
	// user
	SUCCESS_DEFAULT(HttpStatus.OK, "요청을 성공적으로 처리했어요."),
	SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입에 성공했습니다."),
	SUCCESS_GET_ATTRACTIONS(HttpStatus.OK, "전체 관광지 조회에 성공했습니다."),
	SUCCESS_GET_ATTRACTIONS_BY_TYPE(HttpStatus.OK, "콘텐츠 타입별 관광지 조회에 성공했습니다."),
	SUCCESS_GET_CONTENT_TYPES(HttpStatus.OK, "콘텐츠 타입 목록 조회에 성공했습니다."),
	SUCCESS_LOGOUT(HttpStatus.OK, "로그아웃에 성공했습니다."),
	SUCCESS_GET_ATTRACTIONS_IN_BOUNDS(HttpStatus.OK,"지도의 범위 내 관광지 조회 성공"),
	SUCCESS_UPDATE_NICKNAME(HttpStatus.OK,"닉네임 변경 성공"),
	SUCCESS_GET_ATTRACTIONS_BY_CATEGORY(HttpStatus.OK,"콘텐츠 타입별 관광지 조회 성공");



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