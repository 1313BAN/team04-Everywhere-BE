package com.ssafy.enjoytrip.everywhere.board.dto.response;

public record BoardSimpleResponse(
        Long id,
        String title,
        String writer,
        String createdAt
) {
}