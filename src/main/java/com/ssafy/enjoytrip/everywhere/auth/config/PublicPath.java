package com.ssafy.enjoytrip.everywhere.auth.config;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PublicPath {

	// USER
	USER_CREATE("/api/user/signup"),

	// AUTH
	AUTH_LOGIN("/api/auth/login"),
	AUTH_REFRESH("/api/auth/refresh");

	private final String path;

	public static boolean matches(String requestPath) {
		return Arrays.stream(values())
			.anyMatch(endpoint -> endpoint.path.equals(requestPath));
	}
}