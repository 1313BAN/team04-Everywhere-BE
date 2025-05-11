package com.ssafy.enjoytrip.everywhere.auth.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ssafy.enjoytrip.everywhere.auth.domain.Authenticator;
import com.ssafy.enjoytrip.everywhere.auth.dto.request.LoginRequest;
import com.ssafy.enjoytrip.everywhere.auth.jwt.JwtToken;
import com.ssafy.enjoytrip.everywhere.auth.jwt.JwtTokenProvider;
import com.ssafy.enjoytrip.everywhere.common.constants.Role;
import com.ssafy.enjoytrip.everywhere.user.domain.User;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	private final LoginRequest validRequest = new LoginRequest("user1", "password123");
	private final User testUserDomain = new User(
		"user1", "encoded_pw", "닉네임", Role.USER
	);
	@Mock
	private JwtTokenProvider jwtTokenProvider;
	@Mock
	private Authenticator authenticator;
	@InjectMocks
	private AuthService authService;

	@Nested
	@DisplayName("로그인 성공")
	class LoginSuccess {

		@Test
		@DisplayName("로그인 성공 시 accessToken, refreshToken 반환")
		void login_success() {
			// given
			given(authenticator.authenticate("user1", "password123")).willReturn(testUserDomain);

			JwtToken expectedToken = JwtToken.builder()
				.accessToken("access-token")
				.refreshToken("refresh-token")
				.build();

			given(jwtTokenProvider.generateToken(testUserDomain)).willReturn(expectedToken);

			// when
			JwtToken actualToken = authService.login(validRequest);

			// then
			assertThat(actualToken).isEqualTo(expectedToken);
		}

	}
}