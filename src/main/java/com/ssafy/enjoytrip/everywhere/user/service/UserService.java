package com.ssafy.enjoytrip.everywhere.user.service;

import com.ssafy.enjoytrip.everywhere.board.entity.Board;
import com.ssafy.enjoytrip.everywhere.board.repository.BoardRepository;
import com.ssafy.enjoytrip.everywhere.user.dto.response.ProfileResponse;
import com.ssafy.enjoytrip.everywhere.user.mapper.ProfileMapper;
import org.springframework.stereotype.Service;

import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import com.ssafy.enjoytrip.everywhere.common.exception.ApiException;
import com.ssafy.enjoytrip.everywhere.user.dto.request.SignupRequest;
import com.ssafy.enjoytrip.everywhere.user.entity.UserEntity;
import com.ssafy.enjoytrip.everywhere.user.mapper.UserMapper;
import com.ssafy.enjoytrip.everywhere.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final BoardRepository boardRepository;
	private final UserMapper userMapper;
	private final ProfileMapper profileMapper;

	public void signup(SignupRequest request) {
		validateDuplicatedUserId(request.userId());
		UserEntity userEntity = userMapper.toEntity(request);
		userRepository.save(userEntity);
	}

	private void validateDuplicatedUserId(String userId) {
		if (userRepository.existsByUserId(userId)) {
			throw new ApiException(ErrorCode.DUPLICATED_USER_ID);
		}
	}

	@Transactional(readOnly = true)
	public ProfileResponse getProfile(String userId) {
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
		List<Board> boards = boardRepository.findByWriter_UserId(userId);

		return profileMapper.toResponse(user, boards);
	}
}