package com.ssafy.enjoytrip.everywhere.auth.service;

import com.ssafy.enjoytrip.everywhere.auth.dto.request.LoginRequest;
import com.ssafy.enjoytrip.everywhere.auth.dto.response.LoginResponse;
import com.ssafy.enjoytrip.everywhere.auth.jwt.JwtTokenProvider;
import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import com.ssafy.enjoytrip.everywhere.common.exception.InvalidTokenException;
import com.ssafy.enjoytrip.everywhere.user.entity.User;
import com.ssafy.enjoytrip.everywhere.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        // 1. 사용자 조회
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new InvalidTokenException(ErrorCode.USER_NOT_FOUND.message()));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidTokenException(ErrorCode.INVALID_CREDENTIALS.message());
        }

        // 3. Access & Refresh Token 생성
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        // 4. Refresh Token 저장
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        // 5. 응답 반환
        return new LoginResponse(accessToken, refreshToken);
    }}