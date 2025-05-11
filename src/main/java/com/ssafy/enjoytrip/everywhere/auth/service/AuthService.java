package com.ssafy.enjoytrip.everywhere.auth.service;

import com.ssafy.enjoytrip.everywhere.auth.domain.Authenticator;
import com.ssafy.enjoytrip.everywhere.auth.domain.User;
import com.ssafy.enjoytrip.everywhere.auth.dto.request.LoginRequest;
import com.ssafy.enjoytrip.everywhere.auth.jwt.JwtToken;
import com.ssafy.enjoytrip.everywhere.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final Authenticator authenticator;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtToken login(LoginRequest request) {
        User user = authenticator.authenticate(request.userId(), request.password());
        return jwtTokenProvider.generateToken(user);
    }
}