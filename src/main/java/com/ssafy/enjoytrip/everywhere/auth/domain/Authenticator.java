package com.ssafy.enjoytrip.everywhere.auth.domain;

import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import com.ssafy.enjoytrip.everywhere.common.exception.ApiException;
import com.ssafy.enjoytrip.everywhere.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Authenticator {

    private final PasswordEncoder passwordEncoder;

    public void verifyPassword(UserEntity userEntity, String rawPassword) {
        if (!userEntity.isPasswordMatch(rawPassword, passwordEncoder)) {
            throw new ApiException(ErrorCode.INVALID_CREDENTIALS);
        }
    }
}