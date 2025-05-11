package com.ssafy.enjoytrip.everywhere.user.service;

import org.springframework.stereotype.Service;

import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import com.ssafy.enjoytrip.everywhere.common.exception.ApiException;
import com.ssafy.enjoytrip.everywhere.user.dto.request.SignupRequest;
import com.ssafy.enjoytrip.everywhere.user.entity.UserEntity;
import com.ssafy.enjoytrip.everywhere.user.mapper.UserMapper;
import com.ssafy.enjoytrip.everywhere.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final UserMapper mapper;

	public void signup(SignupRequest request) {
		validateDuplicatedUserId(request.userId());
		UserEntity userEntity = mapper.toEntity(request);
		userRepository.save(userEntity);
	}

	private void validateDuplicatedUserId(String userId) {
		if (userRepository.existsByUserId(userId)) {
			throw new ApiException(ErrorCode.DUPLICATED_USER_ID);
		}
	}
}