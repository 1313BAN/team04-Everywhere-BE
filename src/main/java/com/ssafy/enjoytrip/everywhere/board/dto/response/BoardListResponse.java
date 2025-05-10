package com.ssafy.enjoytrip.everywhere.board.dto.response;

import java.util.List;

public record BoardListResponse(
        List<BoardSimpleResponse> boards
) {
}