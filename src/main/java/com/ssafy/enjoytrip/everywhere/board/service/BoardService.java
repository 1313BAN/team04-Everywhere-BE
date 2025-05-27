package com.ssafy.enjoytrip.everywhere.board.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ssafy.enjoytrip.everywhere.board.dto.request.BoardCreateRequest;
import com.ssafy.enjoytrip.everywhere.board.dto.request.BoardModifyRequest;
import com.ssafy.enjoytrip.everywhere.board.dto.response.BoardDetailResponse;
import com.ssafy.enjoytrip.everywhere.board.dto.response.BoardListResponse;
import com.ssafy.enjoytrip.everywhere.board.dto.response.BoardSimpleResponse;
import com.ssafy.enjoytrip.everywhere.board.entity.Board;
import com.ssafy.enjoytrip.everywhere.board.mapper.BoardMapper;
import com.ssafy.enjoytrip.everywhere.board.repository.BoardRepository;
import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import com.ssafy.enjoytrip.everywhere.common.exception.ApiException;
import com.ssafy.enjoytrip.everywhere.image.service.ImageUploadService;
import com.ssafy.enjoytrip.everywhere.user.entity.UserEntity;
import com.ssafy.enjoytrip.everywhere.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class BoardService {
	private final UserRepository userRepository;
	private final BoardRepository boardRepository;
	private final ImageUploadService imageUploader;

	public BoardListResponse findAll() {
		List<Board> boards = boardRepository.findAllWithWriter();
		List<BoardSimpleResponse> simpleResponses = boards.stream()
			.map(board -> new BoardSimpleResponse(
				board.getId(),
				board.getTitle(),
				board.getWriter().getNickname(),
				board.getCreatedAt()
			)).collect(Collectors.toList());
		return new BoardListResponse(simpleResponses);
	}

	public BoardDetailResponse findById(Long id) {
		Board board = boardRepository.findByIdWithWriter(id)
			.orElseThrow(() -> new ApiException(ErrorCode.BOARD_NOT_FOUND));
		return new BoardDetailResponse(
			board.getId(),
			board.getTitle(),
			board.getContent(),
			board.getWriter().getNickname(),
			board.getImageUrls(),
			board.getCreatedAt()
		);
	}

	public BoardDetailResponse create(BoardCreateRequest request, List<MultipartFile> images, String tokenUserId) {
		UserEntity writer = getUserOrThrow(tokenUserId);
		List<String> uploadedUrls = imageUploader.upload(images);

		Board board = Board.builder()
				.title(request.title())
				.content(request.content())
				.writer(writer)
				.imageUrls(uploadedUrls)
				.build();

		boardRepository.save(board);

		return new BoardDetailResponse(
				board.getId(),
				board.getTitle(),
				board.getContent(),
				board.getWriter().getNickname(),
				board.getImageUrls(),
				board.getCreatedAt()
		);
	}

	private UserEntity getUserOrThrow(String userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
	}

	public BoardDetailResponse update(Long id, BoardModifyRequest request, List<MultipartFile> images, String userId) {
		Board board = boardRepository.findByIdWithWriter(id)
			.orElseThrow(() -> new ApiException(ErrorCode.BOARD_NOT_FOUND));

		String writerId = boardRepository.findWriterUserIdByBoardId(id);
		if (!writerId.equals(userId)) {
			throw new ApiException(ErrorCode.NON_VALIDATED_USER);
		}

		List<String> urls = imageUploader.upload(images);
		board.update(request.title(), request.content(), urls);

		return new BoardDetailResponse(
			board.getId(),
			board.getTitle(),
			board.getContent(),
			board.getWriter().getNickname(),
			board.getImageUrls(),
			board.getCreatedAt()
		);
	}

	public void delete(Long id, String userId) {
		Board board = boardRepository.findByIdWithWriter(id)
			.orElseThrow(() -> new ApiException(ErrorCode.BOARD_NOT_FOUND));

		UserEntity writer = getUserOrThrow(userId);
		if (!board.getWriter().equals(writer)) {
			throw new ApiException(ErrorCode.NON_VALIDATED_USER);
		}

		boardRepository.delete(board);
	}
}