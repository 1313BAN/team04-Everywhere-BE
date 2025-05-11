package com.ssafy.enjoytrip.everywhere.auth.controller;

import com.ssafy.enjoytrip.everywhere.auth.dto.request.LoginRequest;
import com.ssafy.enjoytrip.everywhere.auth.dto.response.LoginResponse;
import com.ssafy.enjoytrip.everywhere.auth.jwt.JwtToken;
import com.ssafy.enjoytrip.everywhere.auth.mapper.AuthMapper;
import com.ssafy.enjoytrip.everywhere.auth.service.AuthService;
import com.ssafy.enjoytrip.everywhere.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthMapper authMapper;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        JwtToken token = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(authMapper.toResponse(token)));    }
}