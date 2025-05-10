package com.ssafy.enjoytrip.everywhere.auth.service;

import com.ssafy.enjoytrip.everywhere.auth.domain.Authenticator;
import com.ssafy.enjoytrip.everywhere.auth.dto.request.LoginRequest;
import com.ssafy.enjoytrip.everywhere.auth.dto.response.LoginResponse;
import com.ssafy.enjoytrip.everywhere.auth.jwt.JwtTokenProvider;
import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import com.ssafy.enjoytrip.everywhere.common.exception.InvalidTokenException;
import com.ssafy.enjoytrip.everywhere.user.entity.User;
import com.ssafy.enjoytrip.everywhere.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private Authenticator authenticator;

    @InjectMocks
    private AuthService authService;

    private final LoginRequest validRequest = new LoginRequest("user1", "password123");

    private final User testUser = User.builder()
            .userId("user1")
            .password("encoded_pw")
            .nickname("닉네임")
            .build();

    @Nested
    @DisplayName("로그인 성공")
    class LoginSuccess {

        @Test
        @DisplayName("정상 로그인 시 accessToken, refreshToken 반환")
        void login_success() {
            // given
            given(userRepository.findById("user1")).willReturn(Optional.of(testUser));
            willDoNothing().given(authenticator).verifyPassword(testUser, "password123");
            given(jwtTokenProvider.createAccessToken(testUser)).willReturn("access-token");
            given(jwtTokenProvider.createRefreshToken(testUser)).willReturn("refresh-token");

            // when
            LoginResponse response = authService.login(validRequest);

            // then
            assertThat(response.accessToken()).isEqualTo("access-token");
            assertThat(response.refreshToken()).isEqualTo("refresh-token");

            then(userRepository).should().updateRefreshToken("user1", "refresh-token");
        }
    }

    @Nested
    @DisplayName("로그인 실패")
    class LoginFailure {

        @Test
        @DisplayName("존재하지 않는 userId")
        void user_not_found() {
            // given
            given(userRepository.findById("user1")).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> authService.login(validRequest))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessageContaining("사용자를 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("비밀번호 불일치")
        void password_mismatch() {
            // given
            given(userRepository.findById("user1")).willReturn(Optional.of(testUser));
            willThrow(new InvalidTokenException(ErrorCode.INVALID_CREDENTIALS))
                    .given(authenticator).verifyPassword(testUser, "password123");

            // when & then
            assertThatThrownBy(() -> authService.login(validRequest))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessageContaining("아이디 또는 비밀번호가 올바르지 않습니다.");
        }
    }
}