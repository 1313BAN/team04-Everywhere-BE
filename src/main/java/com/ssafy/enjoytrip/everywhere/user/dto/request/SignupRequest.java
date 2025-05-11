package com.ssafy.enjoytrip.everywhere.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
	@NotBlank String userId,
	@NotBlank String password,
	@NotBlank String nickname
) {
}