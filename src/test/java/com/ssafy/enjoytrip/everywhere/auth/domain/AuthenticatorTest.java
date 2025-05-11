package com.ssafy.enjoytrip.everywhere.auth.domain;

import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import com.ssafy.enjoytrip.everywhere.common.constants.Role;
import com.ssafy.enjoytrip.everywhere.common.exception.ApiException;
import com.ssafy.enjoytrip.everywhere.user.entity.UserEntity;
import com.ssafy.enjoytrip.everywhere.user.mapper.UserMapper;
import com.ssafy.enjoytrip.everywhere.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthenticatorTest {

    @Mock
    private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private Authenticator authenticator;

    private final UserEntity testUserEntity = UserEntity.builder()
            .userId("user1")
            .password("encoded_pw")
            .nickname("닉네임")
            .role(Role.USER)
            .build();

    private final User testUserDomain = new User("user1", "encoded_pw", "닉네임", Role.USER);

    @Nested
    @DisplayName("로그인 실패")
    class LoginFailure {

        @Test
        @DisplayName("존재하지 않는 userId")
        void user_not_found() {
            // given
            given(userRepository.findById("user1")).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> authenticator.authenticate("user1", "password123"))
                    .isInstanceOf(ApiException.class)
                    .hasMessage(ErrorCode.INVALID_CREDENTIALS.message());
        }

        @Test
        @DisplayName("비밀번호 불일치")
        void password_mismatch() {
            // given
            given(userRepository.findById("user1")).willReturn(Optional.of(testUserEntity));
            given(userMapper.toDomain(testUserEntity)).willReturn(testUserDomain);
            given(passwordEncoder.matches("password123", "encoded_pw")).willReturn(false);

            // when & then
            assertThatThrownBy(() -> authenticator.authenticate("user1", "password123"))
                    .isInstanceOf(ApiException.class)
                    .hasMessage(ErrorCode.INVALID_CREDENTIALS.message());
        }
    }
}