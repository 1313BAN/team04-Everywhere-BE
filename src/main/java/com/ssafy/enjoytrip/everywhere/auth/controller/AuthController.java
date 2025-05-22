package com.ssafy.enjoytrip.everywhere.auth.controller;

import com.ssafy.enjoytrip.everywhere.auth.jwt.JwtConstants;
import com.ssafy.enjoytrip.everywhere.auth.jwt.JwtTokenResolver;
import com.ssafy.enjoytrip.everywhere.auth.jwt.JwtUtils;
import com.ssafy.enjoytrip.everywhere.auth.jwt.blacklist.service.JwtBlacklistService;
import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import com.ssafy.enjoytrip.everywhere.common.constants.SuccessCode;
import com.ssafy.enjoytrip.everywhere.common.exception.ApiException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.ssafy.enjoytrip.everywhere.auth.dto.request.LoginRequest;
import com.ssafy.enjoytrip.everywhere.auth.dto.response.LoginResponse;
import com.ssafy.enjoytrip.everywhere.auth.jwt.JwtToken;
import com.ssafy.enjoytrip.everywhere.auth.mapper.AuthMapper;
import com.ssafy.enjoytrip.everywhere.auth.service.AuthService;
import com.ssafy.enjoytrip.everywhere.common.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final JwtTokenResolver tokenResolver;
	private final JwtUtils jwtUtils;
	private final JwtBlacklistService blacklistService;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
		JwtToken token = authService.login(request);
		String nickname = jwtUtils.getNickname(token.getAccessToken());
		LoginResponse response = new LoginResponse(token.getAccessToken(), token.getRefreshToken(), nickname);
		return ResponseEntity.ok(ApiResponse.success(response));

	}

	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal(expression = "username") String userId,
													@RequestHeader("Authorization") String header) {
		String token = tokenResolver.resolve(header);

		if (token == null) {
			throw new ApiException(ErrorCode.TOKEN_INVALID);
		}

		Date expiration = jwtUtils.getExpiration(token);
		blacklistService.blacklist(token, expiration);

		return ResponseEntity.ok(ApiResponse.success(SuccessCode.SUCCESS_LOGOUT));
	}

}