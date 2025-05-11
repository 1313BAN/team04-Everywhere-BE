package com.ssafy.enjoytrip.everywhere.user.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ssafy.enjoytrip.everywhere.common.constants.Role;
import com.ssafy.enjoytrip.everywhere.user.domain.User;
import com.ssafy.enjoytrip.everywhere.user.dto.request.SignupRequest;
import com.ssafy.enjoytrip.everywhere.user.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {
	private final PasswordEncoder passwordEncoder;

	public UserEntity toEntity(SignupRequest request) {
		return UserEntity.builder()
			.userId(request.userId())
			.password(passwordEncoder.encode(request.password()))
			.nickname(request.nickname())
			.role(Role.USER)
			.build();
	}

	public User toDomain(UserEntity entity) {
		return new User(
			entity.getUserId(),
			entity.getPassword(),
			entity.getNickname(),
			entity.getRole()
		);
	}
}