package com.ssafy.enjoytrip.everywhere.auth.service;

import com.ssafy.enjoytrip.everywhere.auth.domain.Authenticator;
import com.ssafy.enjoytrip.everywhere.auth.dto.request.LoginRequest;
import com.ssafy.enjoytrip.everywhere.auth.dto.response.LoginResponse;
import com.ssafy.enjoytrip.everywhere.auth.jwt.JwtTokenProvider;
import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import com.ssafy.enjoytrip.everywhere.common.exception.InvalidTokenException;
import com.ssafy.enjoytrip.everywhere.user.entity.User;
import com.ssafy.enjoytrip.everywhere.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final Authenticator authenticator;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new InvalidTokenException(ErrorCode.USER_NOT_FOUND));

        authenticator.verifyPassword(user, request.password());
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        userRepository.updateRefreshToken(user.getUserId(), refreshToken);

        return new LoginResponse(accessToken, refreshToken);
    }
}