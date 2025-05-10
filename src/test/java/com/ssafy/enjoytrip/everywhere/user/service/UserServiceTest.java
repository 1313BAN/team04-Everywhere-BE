package com.ssafy.enjoytrip.everywhere.user.service;

import com.ssafy.enjoytrip.everywhere.user.dto.request.SignupRequest;
import com.ssafy.enjoytrip.everywhere.user.entity.User;
import com.ssafy.enjoytrip.everywhere.user.mapper.UserMapper;
import com.ssafy.enjoytrip.everywhere.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;

    @InjectMocks private UserService userService;

    private final SignupRequest validRequest = new SignupRequest(
            "testUser", "securePass123", "nickname"
    );

    @Nested
    @DisplayName("회원가입 성공 케이스")
    class SignupSuccess {

        @Test
        @DisplayName("중복 userId가 없을 때, 회원가입 성공")
        void signup_success() {
            // given
            given(userRepository.existsByUserId(validRequest.userId()))
                    .willReturn(false);

            User mockUser = mock(User.class);
            given(userMapper.toEntity(validRequest)).willReturn(mockUser);
            given(userRepository.save(mockUser)).willReturn(mockUser);

            // when & then
            assertThatCode(() -> userService.signup(validRequest))
                    .doesNotThrowAnyException();

            then(userRepository).should().save(mockUser);
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