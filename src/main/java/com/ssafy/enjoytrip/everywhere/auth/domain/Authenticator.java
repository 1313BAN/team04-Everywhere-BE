package com.ssafy.enjoytrip.everywhere.auth.domain;

import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import com.ssafy.enjoytrip.everywhere.common.exception.InvalidTokenException;
import com.ssafy.enjoytrip.everywhere.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Authenticator {

    private final PasswordEncoder passwordEncoder;

    public void verifyPassword(User user, String rawPassword) {
        if (!user.isPasswordMatch(rawPassword, passwordEncoder)) {
            throw new InvalidTokenException(ErrorCode.INVALID_CREDENTIALS);
        }
    }
}