package com.ssafy.enjoytrip.everywhere.auth.service;

import com.ssafy.enjoytrip.everywhere.auth.domain.User;
import com.ssafy.enjoytrip.everywhere.auth.dto.request.LoginRequest;
import com.ssafy.enjoytrip.everywhere.auth.dto.response.LoginResponse;
import com.ssafy.enjoytrip.everywhere.auth.jwt.JwtToken;
import com.ssafy.enjoytrip.everywhere.auth.jwt.JwtTokenProvider;
import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import com.ssafy.enjoytrip.everywhere.common.exception.ApiException;
import com.ssafy.enjoytrip.everywhere.user.mapper.UserMapper;
import com.ssafy.enjoytrip.everywhere.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public JwtToken login(LoginRequest request) {
        User user = userRepository.findById(request.userId())
                .map(userMapper::toDomain)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        user.validatePassword(request.password(), passwordEncoder);

        return jwtTokenProvider.generateToken(user);
    }
}