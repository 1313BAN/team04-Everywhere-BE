package com.ssafy.enjoytrip.everywhere.auth.service;

import com.ssafy.enjoytrip.everywhere.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.ssafy.enjoytrip.everywhere.auth.domain.Authenticator;
import com.ssafy.enjoytrip.everywhere.auth.dto.request.LoginRequest;
import com.ssafy.enjoytrip.everywhere.auth.jwt.JwtToken;
import com.ssafy.enjoytrip.everywhere.auth.jwt.JwtTokenProvider;
import com.ssafy.enjoytrip.everywhere.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

	private final Authenticator authenticator;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository; // ← 추가

	public JwtToken login(LoginRequest request) {
		User user = authenticator.authenticate(request.userId(), request.password());
		JwtToken token = jwtTokenProvider.generateToken(user);
		userRepository.updateRefreshToken(user.userId(), token.getRefreshToken());

		return token;
	}
}