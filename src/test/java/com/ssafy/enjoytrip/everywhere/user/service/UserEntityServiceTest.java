package com.ssafy.enjoytrip.everywhere.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ssafy.enjoytrip.everywhere.user.dto.request.SignupRequest;
import com.ssafy.enjoytrip.everywhere.user.entity.UserEntity;
import com.ssafy.enjoytrip.everywhere.user.mapper.UserMapper;
import com.ssafy.enjoytrip.everywhere.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserEntityServiceTest {

	private final SignupRequest validRequest = new SignupRequest(
		"testUser", "securePass123", "nickname"
	);
	@Mock
	private UserRepository userRepository;
	@Mock
	private UserMapper userMapper;
	@InjectMocks
	private UserService userService;

	@Nested
	@DisplayName("회원가입 성공 케이스")
	class SignupSuccess {

		@Test
		@DisplayName("중복 userId가 없을 때, 회원가입 성공")
		void signup_success() {
			// given
			given(userRepository.existsByUserId(validRequest.userId()))
				.willReturn(false);

			UserEntity mockUserEntity = mock(UserEntity.class);
			given(userMapper.toEntity(validRequest)).willReturn(mockUserEntity);
			given(userRepository.save(mockUserEntity)).willReturn(mockUserEntity);

			// when & then
			assertThatCode(() -> userService.signup(validRequest))
				.doesNotThrowAnyException();

			then(userRepository).should().save(mockUserEntity);
		}
	}

	@Nested
	@DisplayName("회원가입 실패 케이스")
	class SignupFailure {

		@Test
		@DisplayName("이미 존재하는 userId일 경우 예외 발생")
		void signup_duplicateUserId_throwsException() {
			// given
			given(userRepository.existsByUserId(validRequest.userId()))
				.willReturn(true);

			// when & then
			assertThatThrownBy(() -> userService.signup(validRequest))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("이미 존재하는 아이디입니다.");

			then(userRepository).should(never()).save(any());
		}
	}
}