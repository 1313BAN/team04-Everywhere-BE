package com.ssafy.enjoytrip.everywhere.board.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record BoardDetailResponse(
	Long id,
	String title,
	String content,
	String writer,
	List<String> imageUrls,
	LocalDateTime createdAt
) {
}