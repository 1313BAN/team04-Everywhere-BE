package com.ssafy.enjoytrip.everywhere.user.controller;

import com.ssafy.enjoytrip.everywhere.user.dto.response.ProfileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.ssafy.enjoytrip.everywhere.common.dto.response.ApiResponse;
import com.ssafy.enjoytrip.everywhere.user.dto.request.SignupRequest;
import com.ssafy.enjoytrip.everywhere.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<Void>> signup(@RequestBody SignupRequest request) {
		userService.signup(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success());
	}

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<ProfileResponse>> getUserProfile(@AuthenticationPrincipal(expression = "username") String userId) {
		ProfileResponse response = userService.getProfile(userId);
		return ResponseEntity.ok(ApiResponse.success(response));
	}
}