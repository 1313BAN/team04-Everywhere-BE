package com.ssafy.enjoytrip.everywhere.board.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ssafy.enjoytrip.everywhere.board.dto.request.BoardCreateRequest;
import com.ssafy.enjoytrip.everywhere.board.dto.response.BoardDetailResponse;
import com.ssafy.enjoytrip.everywhere.board.dto.response.BoardListResponse;
import com.ssafy.enjoytrip.everywhere.board.dto.response.BoardSimpleResponse;
import com.ssafy.enjoytrip.everywhere.board.entity.Board;
import com.ssafy.enjoytrip.everywhere.user.entity.UserEntity;

@Component
public class BoardMapper {

	public static BoardSimpleResponse toSimple(Board board) {
		return new BoardSimpleResponse(
			board.getId(),
			board.getTitle(),
			board.getWriter().getNickname(),
			board.getCreatedAt()
		);
	}

	public static BoardListResponse toList(List<Board> boards) {
		List<BoardSimpleResponse> responseList = boards.stream()
			.map(BoardMapper::toSimple)
			.collect(Collectors.toList());
		return new BoardListResponse(responseList);
	}

	public Board toEntity(BoardCreateRequest request, UserEntity writer, List<String> imageUrls) {
		return Board.builder()
			.title(request.title())
			.content(request.content())
			.writer(writer)
			.imageUrls(imageUrls)
			.build();
	}

	public BoardDetailResponse toDetail(Board board) {
		return new BoardDetailResponse(
			board.getId(),
			board.getTitle(),
			board.getContent(),
			board.getWriter().getNickname(),
			board.getImageUrls(),
			board.getCreatedAt()
		);
	}

}