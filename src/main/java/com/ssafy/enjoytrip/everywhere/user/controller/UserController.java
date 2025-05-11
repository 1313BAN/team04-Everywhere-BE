package com.ssafy.enjoytrip.everywhere.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}