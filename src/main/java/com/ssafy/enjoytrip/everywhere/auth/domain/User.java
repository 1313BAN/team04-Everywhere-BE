package com.ssafy.enjoytrip.everywhere.auth.domain;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import com.ssafy.enjoytrip.everywhere.common.constants.Role;
import com.ssafy.enjoytrip.everywhere.common.exception.ApiException;

public record User(String userId, String password, String nickname, Role role) {

	public void validatePassword(String rawPassword, PasswordEncoder encoder) {
		if (!encoder.matches(rawPassword, password)) {
			throw new ApiException(ErrorCode.INVALID_CREDENTIALS);
		}
	}
}