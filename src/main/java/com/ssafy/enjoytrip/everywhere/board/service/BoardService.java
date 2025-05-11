package com.ssafy.enjoytrip.everywhere.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.enjoytrip.everywhere.board.dto.request.BoardCreateRequest;
import com.ssafy.enjoytrip.everywhere.board.dto.request.BoardModifyRequest;
import com.ssafy.enjoytrip.everywhere.board.dto.response.BoardDetailResponse;
import com.ssafy.enjoytrip.everywhere.board.dto.response.BoardListResponse;
import com.ssafy.enjoytrip.everywhere.board.entity.Board;
import com.ssafy.enjoytrip.everywhere.board.mapper.BoardMapper;
import com.ssafy.enjoytrip.everywhere.board.repository.BoardRepository;
import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import com.ssafy.enjoytrip.everywhere.common.exception.ApiException;
import com.ssafy.enjoytrip.everywhere.image.service.ImageUploadService;
import com.ssafy.enjoytrip.everywhere.user.entity.UserEntity;
import com.ssafy.enjoytrip.everywhere.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BoardService {
	private final UserRepository userRepository;
	private final BoardRepository boardRepository;
	private final ImageUploadService imageUploader;
	private final BoardMapper mapper;

	public BoardListResponse findAll() {
		return BoardMapper.toList(boardRepository.findAll());
	}

	public BoardDetailResponse findById(Long id) {
		Board board = boardRepository.findById(id)
			.orElseThrow(() -> new ApiException(ErrorCode.BOARD_NOT_FOUND));
		return mapper.toDetail(board);
	}

	public BoardDetailResponse create(BoardCreateRequest request, String tokenUserId) {
		UserEntity writer = getUserOrThrow(tokenUserId);
		List<String> uploadedUrls = imageUploader.upload(request.images());

		Board board = mapper.toEntity(request, writer, uploadedUrls);
		boardRepository.save(board);

		return mapper.toDetail(board);
	}

	private UserEntity getUserOrThrow(String userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
	}

	public BoardDetailResponse update(Long id, BoardModifyRequest request, String userId) {
		Board board = boardRepository.findById(id)
			.orElseThrow(() -> new ApiException(ErrorCode.BOARD_NOT_FOUND));

		String writerId = boardRepository.findWriterUserIdByBoardId(id);
		if (!writerId.equals(userId)) {
			throw new ApiException(ErrorCode.NON_VALIDATED_USER);
		}

		List<String> urls = imageUploader.upload(request.images());
		board.update(request.title(), request.content(), urls);
		return mapper.toDetail(board);
	}

	public void delete(Long id, String userId) {
		Board board = boardRepository.findById(id)
			.orElseThrow(() -> new ApiException(ErrorCode.BOARD_NOT_FOUND));

		UserEntity writer = getUserOrThrow(userId);
		if (!board.getWriter().equals(writer)) {
			throw new ApiException(ErrorCode.NON_VALIDATED_USER);
		}

		boardRepository.delete(board);
	}
}