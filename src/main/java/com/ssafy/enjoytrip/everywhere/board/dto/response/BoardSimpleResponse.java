package com.ssafy.enjoytrip.everywhere.board.dto.response;

import java.time.LocalDateTime;

public record BoardSimpleResponse(
	Long id,
	String title,
	String writer,
	LocalDateTime createdAt
) {
}