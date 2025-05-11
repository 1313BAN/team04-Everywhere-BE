package com.ssafy.enjoytrip.everywhere.auth.domain;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import com.ssafy.enjoytrip.everywhere.common.exception.ApiException;
import com.ssafy.enjoytrip.everywhere.user.entity.UserEntity;
import com.ssafy.enjoytrip.everywhere.user.mapper.UserMapper;
import com.ssafy.enjoytrip.everywhere.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class Authenticator {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;

	public User authenticate(String userId, String rawPassword) {
		UserEntity userEntity = userRepository.findById(userId)
			.orElseThrow(() -> new ApiException(ErrorCode.INVALID_CREDENTIALS));

		User user = userMapper.toDomain(userEntity);

		if (!passwordEncoder.matches(rawPassword, user.password())) {
			throw new ApiException(ErrorCode.INVALID_CREDENTIALS);
		}

		return user;
	}
}